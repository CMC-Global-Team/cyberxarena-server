package internetcafe_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class CreateMembershipCardRequestDTO {
    
    @NotBlank(message = "Membership card name is required")
    @Size(max = 100, message = "Membership card name must not exceed 100 characters")
    private String membershipCardName;
    
    private Integer discountId;
    
    private BigDecimal rechargeThreshold = BigDecimal.ZERO;
    
    private Boolean isDefault = false;
    
    // Constructors
    public CreateMembershipCardRequestDTO() {}
    
    public CreateMembershipCardRequestDTO(String membershipCardName, Integer discountId) {
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
        this.rechargeThreshold = BigDecimal.ZERO;
        this.isDefault = false;
    }
    
    public CreateMembershipCardRequestDTO(String membershipCardName, Integer discountId, BigDecimal rechargeThreshold) {
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
        this.rechargeThreshold = rechargeThreshold != null ? rechargeThreshold : BigDecimal.ZERO;
        this.isDefault = false;
    }
    
    public CreateMembershipCardRequestDTO(String membershipCardName, Integer discountId, BigDecimal rechargeThreshold, Boolean isDefault) {
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
        this.rechargeThreshold = rechargeThreshold != null ? rechargeThreshold : BigDecimal.ZERO;
        this.isDefault = isDefault;
    }
    
    // Getters and Setters
    public String getMembershipCardName() {
        return membershipCardName;
    }
    
    public void setMembershipCardName(String membershipCardName) {
        this.membershipCardName = membershipCardName;
    }
    
    public Integer getDiscountId() {
        return discountId;
    }
    
    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public BigDecimal getRechargeThreshold() {
        return rechargeThreshold;
    }
    
    public void setRechargeThreshold(BigDecimal rechargeThreshold) {
        this.rechargeThreshold = rechargeThreshold;
    }
}
