package internetcafe_management.service;

import internetcafe_management.dto.CreateMembershipCardRequestDTO;
import internetcafe_management.dto.MembershipCardDTO;
import internetcafe_management.dto.UpdateMembershipCardRequestDTO;
import internetcafe_management.entity.MembershipCard;
import internetcafe_management.repository.MembershipCardRepository;
import internetcafe_management.repository.discount.DiscountRepository;
import internetcafe_management.exception.ResourceNotFoundException;
import internetcafe_management.exception.DuplicateResourceException;
import internetcafe_management.service.MembershipRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import internetcafe_management.entity.Customer;

@Service
@Transactional
public class MembershipCardService {
    
    @Autowired
    private MembershipCardRepository membershipCardRepository;
    
    @Autowired
    private DiscountRepository discountRepository;
    
    @Autowired
    private MembershipRankService membershipRankService;
    
    @Autowired
    private internetcafe_management.repository.Customer.CustomerRepository customerRepository;
    
    public MembershipCardDTO createMembershipCard(CreateMembershipCardRequestDTO request) {
        // Check if membership card name already exists
        if (membershipCardRepository.existsByMembershipCardName(request.getMembershipCardName())) {
            throw new DuplicateResourceException("Membership card with name '" + request.getMembershipCardName() + "' already exists");
        }
        
        // Validate discount if provided
        if (request.getDiscountId() != null) {
            if (!discountRepository.existsById(request.getDiscountId())) {
                throw new ResourceNotFoundException("Discount with ID " + request.getDiscountId() + " not found");
            }
        }
        
        // If setting as default, unset all other default cards first
        if (request.getIsDefault() != null && request.getIsDefault()) {
            membershipCardRepository.unsetAllDefaultCards();
        }
        
        MembershipCard membershipCard = new MembershipCard();
        membershipCard.setMembershipCardName(request.getMembershipCardName());
        membershipCard.setDiscountId(request.getDiscountId());
        membershipCard.setRechargeThreshold(request.getRechargeThreshold() != null ? request.getRechargeThreshold() : BigDecimal.ZERO);
        membershipCard.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);
        
        MembershipCard savedMembershipCard = membershipCardRepository.save(membershipCard);
        
        // Force flush to ensure the data is persisted before fetching
        membershipCardRepository.flush();
        
        // Clear the persistence context to force fresh data fetch
        // This ensures we get the latest data from database
        if (request.getDiscountId() != null) {
            // Manually load the discount and set it
            var discount = discountRepository.findById(request.getDiscountId()).orElse(null);
            savedMembershipCard.setDiscount(discount);
        }
        
        return convertToDTO(savedMembershipCard);
    }
    
    public MembershipCardDTO getMembershipCardById(Integer id) {
        MembershipCard membershipCard = membershipCardRepository.findByIdWithDiscount(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership card with ID " + id + " not found"));
        return convertToDTO(membershipCard);
    }
    
    public List<MembershipCardDTO> getAllMembershipCards() {
        List<MembershipCard> membershipCards = membershipCardRepository.findAllWithDiscount();
        return membershipCards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public MembershipCardDTO updateMembershipCard(Integer id, UpdateMembershipCardRequestDTO request) {
        MembershipCard membershipCard = membershipCardRepository.findByIdWithDiscount(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership card with ID " + id + " not found"));
        
        // Check if new name already exists (excluding current record)
        if (!membershipCard.getMembershipCardName().equals(request.getMembershipCardName()) &&
            membershipCardRepository.existsByMembershipCardName(request.getMembershipCardName())) {
            throw new DuplicateResourceException("Membership card with name '" + request.getMembershipCardName() + "' already exists");
        }
        
        // Validate discount if provided
        if (request.getDiscountId() != null) {
            if (!discountRepository.existsById(request.getDiscountId())) {
                throw new ResourceNotFoundException("Discount with ID " + request.getDiscountId() + " not found");
            }
        }
        
        // If setting as default, unset all other default cards first
        if (request.getIsDefault() != null && request.getIsDefault()) {
            membershipCardRepository.unsetAllDefaultCards();
        }
        
        membershipCard.setMembershipCardName(request.getMembershipCardName());
        membershipCard.setDiscountId(request.getDiscountId());
        membershipCard.setRechargeThreshold(request.getRechargeThreshold() != null ? request.getRechargeThreshold() : membershipCard.getRechargeThreshold());
        membershipCard.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : membershipCard.getIsDefault());
        
        MembershipCard savedMembershipCard = membershipCardRepository.save(membershipCard);
        
        // Force flush to ensure the data is persisted before fetching
        membershipCardRepository.flush();
        
        // Manually load the discount and set it
        if (request.getDiscountId() != null) {
            var discount = discountRepository.findById(request.getDiscountId()).orElse(null);
            savedMembershipCard.setDiscount(discount);
        }
        
        return convertToDTO(savedMembershipCard);
    }
    
    public Map<String, Object> checkMembershipCardUsage(Integer id) {
        if (!membershipCardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Membership card with ID " + id + " not found");
        }
        
        // Check if membership card is used by any customers
        List<Object[]> usageResults = membershipCardRepository.findCustomersUsingMembershipCard(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("isUsed", !usageResults.isEmpty());
        result.put("usageCount", usageResults.size());
        result.put("customers", usageResults.stream()
            .map(row -> {
                Map<String, Object> customer = new HashMap<>();
                customer.put("customerId", row[0]);
                customer.put("customerName", row[1]);
                customer.put("phoneNumber", row[2]);
                customer.put("balance", row[3]);
                return customer;
            })
            .toList());
        
        return result;
    }
    
    public void deleteMembershipCard(Integer id) {
        if (!membershipCardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Membership card with ID " + id + " not found");
        }
        
        // Lấy thông tin gói thành viên trước khi xóa để log
        MembershipCard membershipCard = membershipCardRepository.findById(id).orElse(null);
        String membershipCardName = membershipCard != null ? membershipCard.getMembershipCardName() : "Unknown";
        
        // Xóa gói thành viên
        membershipCardRepository.deleteById(id);
        
        // Tự động cập nhật rank của tất cả khách hàng sau khi xóa gói thành viên
        try {
            membershipRankService.updateAllCustomersMembershipRank();
            System.out.println("✅ Successfully updated all customer ranks after deleting membership card: " + membershipCardName);
        } catch (Exception e) {
            System.err.println("❌ Error updating customer ranks after deleting membership card: " + e.getMessage());
            // Không throw exception để không ảnh hưởng đến việc xóa gói thành viên
        }
    }
    
    public MembershipCardDTO getDefaultMembershipCard() {
        MembershipCard defaultCard = membershipCardRepository.findByIsDefaultTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No default membership card found"));
        return convertToDTO(defaultCard);
    }
    
    /**
     * Set a membership card as default and update all customers
     */
    public void setDefaultMembershipCard(Integer membershipCardId) {
        // Tìm gói thành viên cần set default
        MembershipCard newDefaultCard = membershipCardRepository.findById(membershipCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership card not found with id: " + membershipCardId));
        
        // Bỏ default của tất cả gói hiện tại
        List<MembershipCard> allCards = membershipCardRepository.findAll();
        for (MembershipCard card : allCards) {
            card.setIsDefault(false);
            membershipCardRepository.save(card);
        }
        
        // Set gói mới làm default và set ngưỡng = 0
        newDefaultCard.setIsDefault(true);
        newDefaultCard.setRechargeThreshold(BigDecimal.ZERO);
        membershipCardRepository.save(newDefaultCard);
        
        System.out.println("✅ Set membership card " + membershipCardId + " as default with threshold 0");
        
        // Cập nhật tất cả khách hàng lên gói mới này
        try {
            membershipRankService.updateAllCustomersMembershipRank();
            System.out.println("✅ Updated all customers to new default membership card");
        } catch (Exception e) {
            System.err.println("❌ Error updating all customers to new default membership card: " + e.getMessage());
            throw new RuntimeException("Failed to update customers to new default membership card", e);
        }
    }
    
    private MembershipCardDTO convertToDTO(MembershipCard membershipCard) {
        MembershipCardDTO dto = new MembershipCardDTO();
        dto.setMembershipCardId(membershipCard.getMembershipCardId());
        dto.setMembershipCardName(membershipCard.getMembershipCardName());
        dto.setDiscountId(membershipCard.getDiscountId());
        dto.setRechargeThreshold(membershipCard.getRechargeThreshold());
        dto.setIsDefault(membershipCard.getIsDefault());
        
        if (membershipCard.getDiscount() != null) {
            dto.setDiscountName(membershipCard.getDiscount().getDiscountName());
            dto.setDiscountType(membershipCard.getDiscount().getDiscountType().toString());
            dto.setDiscountValue(membershipCard.getDiscount().getDiscountValue().doubleValue());
        }
        
        return dto;
    }
    
    /**
     * Lấy danh sách khách hàng phù hợp với gói thành viên mới
     */
    public List<Map<String, Object>> getEligibleCustomersForMembershipCard(Integer membershipCardId) {
        // Lấy thông tin gói thành viên
        MembershipCard membershipCard = membershipCardRepository.findById(membershipCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership card not found with id: " + membershipCardId));
        
        // Lấy tất cả khách hàng
        List<Customer> allCustomers = customerRepository.findAll();
        
        return allCustomers.stream()
                .filter(customer -> {
                    // Tính tổng số tiền nạp của khách hàng
                    BigDecimal totalRecharge = customerRepository.getTotalRechargeAmountByCustomerId(customer.getCustomerId());
                    if (totalRecharge == null) {
                        totalRecharge = BigDecimal.ZERO;
                    }
                    
                    // Kiểm tra xem khách hàng có đủ điều kiện cho gói thành viên mới không
                    return totalRecharge.compareTo(membershipCard.getRechargeThreshold()) >= 0;
                })
                .map(customer -> {
                    BigDecimal totalRecharge = customerRepository.getTotalRechargeAmountByCustomerId(customer.getCustomerId());
                    if (totalRecharge == null) {
                        totalRecharge = BigDecimal.ZERO;
                    }
                    
                    Map<String, Object> customerInfo = new HashMap<>();
                    customerInfo.put("customerId", customer.getCustomerId());
                    customerInfo.put("customerName", customer.getCustomerName());
                    customerInfo.put("phoneNumber", customer.getPhoneNumber());
                    customerInfo.put("currentBalance", customer.getBalance());
                    customerInfo.put("totalRecharge", totalRecharge);
                    customerInfo.put("currentMembershipCardId", customer.getMembershipCardId());
                    
                    // Lấy tên gói thành viên hiện tại và ngưỡng
                    if (customer.getMembershipCardId() != null) {
                        try {
                            MembershipCard currentCard = membershipCardRepository.findById(customer.getMembershipCardId()).orElse(null);
                            if (currentCard != null) {
                                customerInfo.put("currentMembershipCardName", currentCard.getMembershipCardName());
                                customerInfo.put("currentMembershipThreshold", currentCard.getRechargeThreshold());
                            } else {
                                customerInfo.put("currentMembershipCardName", "Unknown");
                                customerInfo.put("currentMembershipThreshold", null);
                            }
                        } catch (Exception e) {
                            customerInfo.put("currentMembershipCardName", "Unknown");
                            customerInfo.put("currentMembershipThreshold", null);
                        }
                    } else {
                        customerInfo.put("currentMembershipCardName", "No membership");
                        customerInfo.put("currentMembershipThreshold", null);
                    }
                    
                    return customerInfo;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Cập nhật khách hàng lên gói thành viên mới
     */
    public void updateCustomersToMembershipCard(Integer membershipCardId, List<Integer> customerIds) {
        // Validate membership card exists
        if (!membershipCardRepository.existsById(membershipCardId)) {
            throw new ResourceNotFoundException("Membership card not found with id: " + membershipCardId);
        }
        
        // Update each customer
        for (Integer customerId : customerIds) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
            
            customer.setMembershipCardId(membershipCardId);
            customerRepository.save(customer);
            
            System.out.println("✅ Updated customer " + customerId + " to membership card " + membershipCardId);
        }
    }
}
