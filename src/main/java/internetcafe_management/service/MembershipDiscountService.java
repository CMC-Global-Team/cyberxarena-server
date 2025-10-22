package internetcafe_management.service;

import internetcafe_management.dto.DiscountCalculationDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.MembershipCard;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.MembershipCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service để xử lý logic tính toán discount dựa trên membership card
 */
@Service
@Transactional
public class MembershipDiscountService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private MembershipCardRepository membershipCardRepository;
    
    /**
     * Tính toán discount cho một khách hàng dựa trên membership card hiện tại
     * @param customerId ID của khách hàng
     * @param originalAmount Số tiền gốc cần tính discount
     * @return Số tiền discount được áp dụng
     */
    public BigDecimal calculateDiscount(Integer customerId, BigDecimal originalAmount) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        if (customer.getMembershipCardId() == null) {
            return BigDecimal.ZERO;
        }
        
        MembershipCard membershipCard = membershipCardRepository.findByIdWithDiscount(customer.getMembershipCardId())
                .orElse(null);
        
        if (membershipCard == null || membershipCard.getDiscount() == null) {
            return BigDecimal.ZERO;
        }
        
        return calculateDiscountAmount(originalAmount, membershipCard.getDiscount().getDiscountType().toString(), 
                                     membershipCard.getDiscount().getDiscountValue());
    }
    
    /**
     * Tính toán số tiền discount dựa trên loại discount và giá trị
     * @param originalAmount Số tiền gốc
     * @param discountType Loại discount (Flat hoặc Percentage)
     * @param discountValue Giá trị discount
     * @return Số tiền discount
     */
    private BigDecimal calculateDiscountAmount(BigDecimal originalAmount, String discountType, BigDecimal discountValue) {
        if (originalAmount == null || discountValue == null) {
            return BigDecimal.ZERO;
        }
        
        if ("Percentage".equals(discountType)) {
            // Tính theo phần trăm
            return originalAmount.multiply(discountValue).divide(BigDecimal.valueOf(100));
        } else {
            // Tính theo số tiền cố định (Flat)
            return discountValue.compareTo(originalAmount) > 0 ? originalAmount : discountValue;
        }
    }
    
    /**
     * Tính toán số tiền phải trả sau khi áp dụng discount
     * @param customerId ID của khách hàng
     * @param originalAmount Số tiền gốc
     * @return Số tiền phải trả sau discount
     */
    public BigDecimal calculateFinalAmount(Integer customerId, BigDecimal originalAmount) {
        BigDecimal discountAmount = calculateDiscount(customerId, originalAmount);
        return originalAmount.subtract(discountAmount);
    }
    
    /**
     * Lấy thông tin discount của membership card hiện tại
     * @param customerId ID của khách hàng
     * @return Thông tin discount hoặc null nếu không có
     */
    public MembershipCard getMembershipDiscountInfo(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        if (customer.getMembershipCardId() == null) {
            return null;
        }
        
        return membershipCardRepository.findByIdWithDiscount(customer.getMembershipCardId())
                .orElse(null);
    }
    
    /**
     * Tính toán discount chi tiết cho một khách hàng
     * @param customerId ID của khách hàng
     * @param originalAmount Số tiền gốc
     * @return DiscountCalculationDTO chứa thông tin chi tiết
     */
    public DiscountCalculationDTO calculateDiscountDetails(Integer customerId, BigDecimal originalAmount) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        DiscountCalculationDTO result = new DiscountCalculationDTO();
        result.setCustomerId(customerId);
        result.setCustomerName(customer.getCustomerName());
        result.setOriginalAmount(originalAmount);
        
        if (customer.getMembershipCardId() == null) {
            result.setDiscountAmount(BigDecimal.ZERO);
            result.setFinalAmount(originalAmount);
            return result;
        }
        
        MembershipCard membershipCard = membershipCardRepository.findByIdWithDiscount(customer.getMembershipCardId())
                .orElse(null);
        
        if (membershipCard == null || membershipCard.getDiscount() == null) {
            result.setDiscountAmount(BigDecimal.ZERO);
            result.setFinalAmount(originalAmount);
            return result;
        }
        
        result.setMembershipCardId(membershipCard.getMembershipCardId());
        result.setMembershipCardName(membershipCard.getMembershipCardName());
        result.setDiscountType(membershipCard.getDiscount().getDiscountType().toString());
        result.setDiscountValue(membershipCard.getDiscount().getDiscountValue());
        result.setDiscountName(membershipCard.getDiscount().getDiscountName());
        
        BigDecimal discountAmount = calculateDiscountAmount(originalAmount, 
                                                          membershipCard.getDiscount().getDiscountType().toString(), 
                                                          membershipCard.getDiscount().getDiscountValue());
        
        result.setDiscountAmount(discountAmount);
        result.setFinalAmount(originalAmount.subtract(discountAmount));
        
        return result;
    }
}
