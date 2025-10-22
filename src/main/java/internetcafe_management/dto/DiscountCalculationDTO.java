package internetcafe_management.dto;

import java.math.BigDecimal;

/**
 * DTO chứa thông tin tính toán discount
 */
public class DiscountCalculationDTO {
    
    private Integer customerId;
    private String customerName;
    private Integer membershipCardId;
    private String membershipCardName;
    private BigDecimal originalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String discountType;
    private BigDecimal discountValue;
    private String discountName;
    
    // Constructors
    public DiscountCalculationDTO() {}
    
    public DiscountCalculationDTO(Integer customerId, String customerName, 
                                Integer membershipCardId, String membershipCardName,
                                BigDecimal originalAmount, BigDecimal discountAmount, 
                                BigDecimal finalAmount) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.membershipCardId = membershipCardId;
        this.membershipCardName = membershipCardName;
        this.originalAmount = originalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
    }
    
    // Getters and Setters
    public Integer getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public Integer getMembershipCardId() {
        return membershipCardId;
    }
    
    public void setMembershipCardId(Integer membershipCardId) {
        this.membershipCardId = membershipCardId;
    }
    
    public String getMembershipCardName() {
        return membershipCardName;
    }
    
    public void setMembershipCardName(String membershipCardName) {
        this.membershipCardName = membershipCardName;
    }
    
    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }
    
    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public BigDecimal getFinalAmount() {
        return finalAmount;
    }
    
    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }
    
    public String getDiscountType() {
        return discountType;
    }
    
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    
    public BigDecimal getDiscountValue() {
        return discountValue;
    }
    
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
    
    public String getDiscountName() {
        return discountName;
    }
    
    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }
}
