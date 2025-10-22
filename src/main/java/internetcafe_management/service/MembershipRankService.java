package internetcafe_management.service;

import internetcafe_management.dto.MembershipRankInfoDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.MembershipCard;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.MembershipCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Service để xử lý logic tự động cập nhật membership rank cho khách hàng
 * dựa trên tổng số tiền nạp
 */
@Service
@Transactional
public class MembershipRankService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private MembershipCardRepository membershipCardRepository;
    
    /**
     * Cập nhật membership rank cho khách hàng dựa trên tổng số tiền nạp (Synchronous)
     * @param customerId ID của khách hàng
     * @param newRechargeAmount Số tiền nạp mới
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateMembershipRankSync(Integer customerId, BigDecimal newRechargeAmount) {
        try {
            updateMembershipRankInternal(customerId, newRechargeAmount);
        } catch (Exception e) {
            System.err.println("❌ Sync update failed, trying async: " + e.getMessage());
            // Fallback to async if sync fails
            updateMembershipRank(customerId, newRechargeAmount);
        }
    }
    
    /**
     * Internal method for membership rank update
     * @param customerId ID của khách hàng
     * @param newRechargeAmount Số tiền nạp mới
     */
    private void updateMembershipRankInternal(Integer customerId, BigDecimal newRechargeAmount) {
        System.out.println("=== Starting membership rank update for customer: " + customerId + " ===");
        System.out.println("New recharge amount: " + newRechargeAmount);
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        // Tính tổng số tiền nạp hiện tại
        BigDecimal currentTotalRecharge = customerRepository.getTotalRechargeAmountByCustomerId(customerId);
        if (currentTotalRecharge == null) {
            currentTotalRecharge = BigDecimal.ZERO;
        }
        
        System.out.println("Current total recharge: " + currentTotalRecharge);
        System.out.println("Current membership card ID: " + customer.getMembershipCardId());
        
        // Tìm membership card phù hợp dựa trên tổng số tiền nạp
        MembershipCard appropriateCard = findAppropriateMembershipCard(currentTotalRecharge);
        
        System.out.println("Appropriate card found: " + (appropriateCard != null ? 
            appropriateCard.getMembershipCardName() + " (ID: " + appropriateCard.getMembershipCardId() + ")" : "null"));
        
        // Cập nhật membership card cho khách hàng nếu cần
        boolean needsUpdate = false;
        String updateReason = "";
        
        System.out.println("🔍 Comparing membership cards:");
        System.out.println("  Current customer membership card ID: " + customer.getMembershipCardId());
        System.out.println("  Appropriate card ID: " + (appropriateCard != null ? appropriateCard.getMembershipCardId() : "null"));
        System.out.println("  Appropriate card name: " + (appropriateCard != null ? appropriateCard.getMembershipCardName() : "null"));
        System.out.println("  Appropriate card threshold: " + (appropriateCard != null ? appropriateCard.getRechargeThreshold() : "null"));
        
        if (appropriateCard == null) {
            updateReason = "No appropriate card found";
        } else if (customer.getMembershipCardId() == null) {
            needsUpdate = true;
            updateReason = "Customer has no membership card";
        } else if (!Objects.equals(customer.getMembershipCardId(), appropriateCard.getMembershipCardId())) {
            needsUpdate = true;
            updateReason = "Customer needs upgrade from " + customer.getMembershipCardId() + " to " + appropriateCard.getMembershipCardId();
        } else {
            updateReason = "Customer already has the appropriate membership";
        }
        
        System.out.println("Update needed: " + needsUpdate + " - Reason: " + updateReason);
        
        if (needsUpdate && appropriateCard != null) {
            System.out.println("🔄 Updating customer membership from " + customer.getMembershipCardId() + 
                             " to " + appropriateCard.getMembershipCardId());
            
            // Retry mechanism for lock timeout with exponential backoff
            int maxRetries = 5;
            int retryCount = 0;
            boolean success = false;
            
            while (retryCount < maxRetries && !success) {
                try {
                    retryCount++;
                    System.out.println("🔄 Attempt " + retryCount + " to update customer " + customerId + " membership");
                    
                    // Wait before retry (exponential backoff)
                    if (retryCount > 1) {
                        long waitTime = 1000 * (long) Math.pow(2, retryCount - 2); // 1s, 2s, 4s, 8s
                        System.out.println("⏳ Waiting " + waitTime + "ms before retry...");
                        Thread.sleep(waitTime);
                    }
                    
                    // Try direct update query first to avoid lock timeout
                    try {
                        int updatedRows = customerRepository.updateMembershipCardId(customerId, appropriateCard.getMembershipCardId());
                        
                        if (updatedRows > 0) {
                            System.out.println("💾 Direct query update successful - " + updatedRows + " row(s) updated");
                            success = true;
                        } else {
                            System.err.println("❌ Direct query update failed - no rows updated");
                            throw new RuntimeException("No rows updated for customer " + customerId);
                        }
                    } catch (Exception directUpdateError) {
                        System.err.println("❌ Direct update failed, trying entity update: " + directUpdateError.getMessage());
                        
                        // Fallback to entity update if direct query fails
                        Customer freshCustomer = customerRepository.findById(customerId)
                                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
                        
                        freshCustomer.setMembershipCardId(appropriateCard.getMembershipCardId());
                        Customer savedCustomer = customerRepository.save(freshCustomer);
                        
                        System.out.println("💾 Entity update successful as fallback");
                        success = true;
                    }
                    
                    // Double-check by fetching from database
                    Customer verifyCustomer = customerRepository.findById(customerId).orElse(null);
                    if (verifyCustomer != null) {
                        System.out.println("🔍 Database verification - Customer " + customerId + " membership card ID: " + verifyCustomer.getMembershipCardId());
                        System.out.println("🔍 Database verification - Customer " + customerId + " name: " + verifyCustomer.getCustomerName());
                        System.out.println("🔍 Database verification - Customer " + customerId + " balance: " + verifyCustomer.getBalance());
                        
                        if (verifyCustomer.getMembershipCardId().equals(appropriateCard.getMembershipCardId())) {
                            System.out.println("✅ Verification successful - membership card updated correctly");
                        } else {
                            System.err.println("❌ Verification failed - membership card not updated correctly");
                            throw new RuntimeException("Membership card update verification failed");
                        }
                    } else {
                        System.err.println("❌ ERROR: Could not find customer " + customerId + " after update!");
                        throw new RuntimeException("Customer not found after update");
                    }
                    
                    // Log hoặc thông báo về việc cập nhật membership
                    System.out.println("✅ Customer " + customerId + " upgraded to membership: " + 
                                     appropriateCard.getMembershipCardName() + 
                                     " (Total recharge: " + currentTotalRecharge + ")");
                    
                } catch (Exception e) {
                    System.err.println("❌ ERROR saving customer " + customerId + " (attempt " + retryCount + "): " + e.getMessage());
                    
                    // Check if it's a lock timeout error
                    boolean isLockTimeout = e.getMessage() != null && 
                        (e.getMessage().contains("Lock wait timeout") || 
                         e.getMessage().contains("PessimisticLockingFailureException"));
                    
                    if (isLockTimeout && retryCount < maxRetries) {
                        System.out.println("🔒 Lock timeout detected, will retry...");
                    } else if (retryCount < maxRetries) {
                        System.out.println("🔄 Other error, will retry...");
                    } else {
                        System.err.println("❌ FAILED to update customer " + customerId + " after " + maxRetries + " attempts");
                        if (isLockTimeout) {
                            System.err.println("🔒 Final error was lock timeout - customer may need manual rank update");
                        }
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("ℹ️ No membership update needed for customer " + customerId + " - " + updateReason);
        }
        
        System.out.println("=== End membership rank update ===");
    }

    /**
     * Cập nhật membership rank cho khách hàng dựa trên tổng số tiền nạp (Asynchronous - for backward compatibility)
     * @param customerId ID của khách hàng
     * @param newRechargeAmount Số tiền nạp mới
     */
    @Async("membershipRankExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateMembershipRank(Integer customerId, BigDecimal newRechargeAmount) {
        updateMembershipRankSync(customerId, newRechargeAmount);
    }
    
    /**
     * Tìm membership card phù hợp dựa trên tổng số tiền nạp
     * @param totalRechargeAmount Tổng số tiền nạp
     * @return MembershipCard phù hợp hoặc null nếu không tìm thấy
     */
    private MembershipCard findAppropriateMembershipCard(BigDecimal totalRechargeAmount) {
        System.out.println("🔍 Finding appropriate card for total recharge: " + totalRechargeAmount);
        
        // Lấy tất cả membership cards
        List<MembershipCard> allCards = membershipCardRepository.findAll();
        System.out.println("Total membership cards available: " + allCards.size());
        
        // Log tất cả cards với so sánh
        System.out.println("=== Available membership cards ===");
        for (MembershipCard card : allCards) {
            boolean isEligible = card.getRechargeThreshold() != null && 
                               card.getRechargeThreshold().compareTo(totalRechargeAmount) <= 0;
            System.out.println("Card: " + card.getMembershipCardName() + 
                             ", Threshold: " + card.getRechargeThreshold() + 
                             ", ID: " + card.getMembershipCardId() +
                             ", IsDefault: " + card.getIsDefault() +
                             ", Eligible: " + isEligible);
        }
        
        // Tìm card có threshold cao nhất mà khách hàng đạt được
        List<MembershipCard> eligibleCards = allCards.stream()
                .filter(card -> card.getRechargeThreshold() != null && 
                               card.getRechargeThreshold().compareTo(totalRechargeAmount) <= 0)
                .collect(java.util.stream.Collectors.toList());
        
        System.out.println("Eligible cards count: " + eligibleCards.size());
        
        MembershipCard appropriateCard;
        if (eligibleCards.isEmpty()) {
            System.out.println("No eligible cards found, using default");
            appropriateCard = getDefaultMembershipCard();
        } else {
            appropriateCard = eligibleCards.stream()
                    .max((card1, card2) -> card1.getRechargeThreshold().compareTo(card2.getRechargeThreshold()))
                    .orElse(getDefaultMembershipCard());
        }
        
        if (appropriateCard != null) {
            System.out.println("✅ Selected card: " + appropriateCard.getMembershipCardName() + 
                             " with threshold: " + appropriateCard.getRechargeThreshold());
        } else {
            System.out.println("❌ No appropriate card found, using default");
        }
        
        return appropriateCard;
    }
    
    /**
     * Lấy membership card mặc định
     * @return MembershipCard mặc định
     */
    private MembershipCard getDefaultMembershipCard() {
        MembershipCard defaultCard = membershipCardRepository.findByIsDefaultTrue()
                .orElse(null);
        
        if (defaultCard != null) {
            System.out.println("Default card found: " + defaultCard.getMembershipCardName() + 
                             " (ID: " + defaultCard.getMembershipCardId() + ")");
        } else {
            System.out.println("❌ No default card found!");
        }
        
        return defaultCard;
    }
    
    /**
     * Debug method để kiểm tra dữ liệu membership cards
     */
    public void debugMembershipCards() {
        System.out.println("=== DEBUG: Membership Cards ===");
        List<MembershipCard> allCards = membershipCardRepository.findAll();
        
        if (allCards.isEmpty()) {
            System.out.println("❌ No membership cards found in database!");
            return;
        }
        
        System.out.println("Total membership cards: " + allCards.size());
        for (MembershipCard card : allCards) {
            System.out.println("ID: " + card.getMembershipCardId() + 
                             ", Name: " + card.getMembershipCardName() + 
                             ", Threshold: " + card.getRechargeThreshold() + 
                             ", Is Default: " + card.getIsDefault());
        }
        System.out.println("=== END DEBUG ===");
    }
    
    /**
     * Cập nhật membership rank cho tất cả khách hàng
     * (Dùng để chạy batch job hoặc khi cần cập nhật hàng loạt)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAllCustomersMembershipRank() {
        System.out.println("=== Starting bulk membership rank update ===");
        
        List<Customer> allCustomers = customerRepository.findAll();
        System.out.println("Total customers to process: " + allCustomers.size());
        
        int updatedCount = 0;
        for (Customer customer : allCustomers) {
            try {
                BigDecimal totalRecharge = customerRepository.getTotalRechargeAmountByCustomerId(customer.getCustomerId());
                if (totalRecharge == null) {
                    totalRecharge = BigDecimal.ZERO;
                }
                
                System.out.println("Processing customer " + customer.getCustomerId() + 
                                 " with total recharge: " + totalRecharge);
                
                MembershipCard appropriateCard = findAppropriateMembershipCard(totalRecharge);
                
                if (appropriateCard != null && 
                    (customer.getMembershipCardId() == null || 
                     !customer.getMembershipCardId().equals(appropriateCard.getMembershipCardId()))) {
                    
                    System.out.println("🔄 Updating customer " + customer.getCustomerId() + 
                                     " from " + customer.getMembershipCardId() + 
                                     " to " + appropriateCard.getMembershipCardId());
                    
                    customer.setMembershipCardId(appropriateCard.getMembershipCardId());
                    customerRepository.save(customer);
                    updatedCount++;
                    
                    System.out.println("✅ Updated customer " + customer.getCustomerId() + 
                                     " to " + appropriateCard.getMembershipCardName());
                } else {
                    System.out.println("ℹ️ No update needed for customer " + customer.getCustomerId());
                }
            } catch (Exception e) {
                System.err.println("❌ Error updating customer " + customer.getCustomerId() + ": " + e.getMessage());
            }
        }
        
        System.out.println("=== End bulk membership rank update - Updated " + updatedCount + " customers ===");
    }
    
    /**
     * Lấy thông tin membership card hiện tại của khách hàng
     * @param customerId ID của khách hàng
     * @return MembershipCard hiện tại
     */
    public MembershipCard getCurrentMembershipCard(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        if (customer.getMembershipCardId() == null) {
            return getDefaultMembershipCard();
        }
        
        return membershipCardRepository.findById(customer.getMembershipCardId())
                .orElse(getDefaultMembershipCard());
    }
    
    /**
     * Tính toán membership card tiếp theo mà khách hàng có thể đạt được
     * @param customerId ID của khách hàng
     * @return MembershipCard tiếp theo hoặc null nếu đã đạt level cao nhất
     */
    public MembershipCard getNextMembershipCard(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        final BigDecimal totalRecharge = customerRepository.getTotalRechargeAmountByCustomerId(customerId);
        final BigDecimal finalTotalRecharge = totalRecharge != null ? totalRecharge : BigDecimal.ZERO;
        
        // Tìm card có threshold cao hơn tổng nạp hiện tại
        return membershipCardRepository.findAll().stream()
                .filter(card -> card.getRechargeThreshold() != null && 
                               card.getRechargeThreshold().compareTo(finalTotalRecharge) > 0)
                .min((card1, card2) -> card1.getRechargeThreshold().compareTo(card2.getRechargeThreshold()))
                .orElse(null);
    }
    
    /**
     * Lấy thông tin chi tiết về membership rank của khách hàng
     * @param customerId ID của khách hàng
     * @return MembershipRankInfoDTO chứa thông tin chi tiết
     */
    public MembershipRankInfoDTO getMembershipRankInfo(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        BigDecimal totalRecharge = customerRepository.getTotalRechargeAmountByCustomerId(customerId);
        if (totalRecharge == null) {
            totalRecharge = BigDecimal.ZERO;
        }
        
        MembershipCard currentCard = getCurrentMembershipCard(customerId);
        MembershipCard nextCard = getNextMembershipCard(customerId);
        
        MembershipRankInfoDTO rankInfo = new MembershipRankInfoDTO();
        rankInfo.setCustomerId(customerId);
        rankInfo.setCustomerName(customer.getCustomerName());
        rankInfo.setCurrentTotalRecharge(totalRecharge);
        rankInfo.setCurrentBalance(customer.getBalance());
        
        if (currentCard != null) {
            rankInfo.setCurrentMembershipCardId(currentCard.getMembershipCardId());
            rankInfo.setCurrentMembershipCardName(currentCard.getMembershipCardName());
        }
        
        if (nextCard != null) {
            rankInfo.setNextMembershipCardId(nextCard.getMembershipCardId());
            rankInfo.setNextMembershipCardName(nextCard.getMembershipCardName());
            rankInfo.setNextMembershipCardThreshold(nextCard.getRechargeThreshold());
            
            // Tính số tiền cần nạp thêm để đạt rank tiếp theo
            BigDecimal amountNeeded = nextCard.getRechargeThreshold().subtract(totalRecharge);
            rankInfo.setAmountNeededForNextRank(amountNeeded.compareTo(BigDecimal.ZERO) > 0 ? amountNeeded : BigDecimal.ZERO);
        }
        
        return rankInfo;
    }
}
