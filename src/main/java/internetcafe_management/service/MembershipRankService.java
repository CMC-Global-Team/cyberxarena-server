package internetcafe_management.service;

import internetcafe_management.dto.MembershipRankInfoDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.MembershipCard;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.MembershipCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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
     * Cập nhật membership rank cho khách hàng dựa trên tổng số tiền nạp
     * @param customerId ID của khách hàng
     * @param newRechargeAmount Số tiền nạp mới
     */
    public void updateMembershipRank(Integer customerId, BigDecimal newRechargeAmount) {
        System.out.println("=== Starting membership rank update for customer: " + customerId + " ===");
        
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
        if (appropriateCard != null && 
            (customer.getMembershipCardId() == null || 
             !customer.getMembershipCardId().equals(appropriateCard.getMembershipCardId()))) {
            
            System.out.println("Updating customer membership from " + customer.getMembershipCardId() + 
                             " to " + appropriateCard.getMembershipCardId());
            
            customer.setMembershipCardId(appropriateCard.getMembershipCardId());
            customerRepository.save(customer);
            
            // Log hoặc thông báo về việc cập nhật membership
            System.out.println("✅ Customer " + customerId + " upgraded to membership: " + 
                             appropriateCard.getMembershipCardName() + 
                             " (Total recharge: " + currentTotalRecharge + ")");
        } else {
            System.out.println("❌ No membership update needed for customer " + customerId);
        }
        
        System.out.println("=== End membership rank update ===");
    }
    
    /**
     * Tìm membership card phù hợp dựa trên tổng số tiền nạp
     * @param totalRechargeAmount Tổng số tiền nạp
     * @return MembershipCard phù hợp hoặc null nếu không tìm thấy
     */
    private MembershipCard findAppropriateMembershipCard(BigDecimal totalRechargeAmount) {
        System.out.println("Finding appropriate card for total recharge: " + totalRechargeAmount);
        
        // Lấy tất cả membership cards
        List<MembershipCard> allCards = membershipCardRepository.findAll();
        System.out.println("Total membership cards available: " + allCards.size());
        
        // Log tất cả cards
        for (MembershipCard card : allCards) {
            System.out.println("Card: " + card.getMembershipCardName() + 
                             ", Threshold: " + card.getRechargeThreshold() + 
                             ", ID: " + card.getMembershipCardId());
        }
        
        // Tìm card có threshold cao nhất mà khách hàng đạt được
        MembershipCard appropriateCard = allCards.stream()
                .filter(card -> card.getRechargeThreshold() != null && 
                               card.getRechargeThreshold().compareTo(totalRechargeAmount) <= 0)
                .max((card1, card2) -> card1.getRechargeThreshold().compareTo(card2.getRechargeThreshold()))
                .orElse(getDefaultMembershipCard());
        
        if (appropriateCard != null) {
            System.out.println("Selected card: " + appropriateCard.getMembershipCardName() + 
                             " with threshold: " + appropriateCard.getRechargeThreshold());
        } else {
            System.out.println("No appropriate card found, using default");
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
    public void updateAllCustomersMembershipRank() {
        List<Customer> allCustomers = customerRepository.findAll();
        
        for (Customer customer : allCustomers) {
            BigDecimal totalRecharge = customerRepository.getTotalRechargeAmountByCustomerId(customer.getCustomerId());
            if (totalRecharge == null) {
                totalRecharge = BigDecimal.ZERO;
            }
            
            MembershipCard appropriateCard = findAppropriateMembershipCard(totalRecharge);
            
            if (appropriateCard != null && 
                (customer.getMembershipCardId() == null || 
                 !customer.getMembershipCardId().equals(appropriateCard.getMembershipCardId()))) {
                
                customer.setMembershipCardId(appropriateCard.getMembershipCardId());
                customerRepository.save(customer);
            }
        }
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
