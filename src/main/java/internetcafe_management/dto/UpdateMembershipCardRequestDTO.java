package internetcafe_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateMembershipCardRequestDTO {
    
    @NotBlank(message = "Membership card name is required")
    @Size(max = 100, message = "Membership card name must not exceed 100 characters")
    private String membershipCardName;
    
    private Integer discountId;
    
    private Boolean isDefault;
    
    // Constructors
    public UpdateMembershipCardRequestDTO() {}
    
    public UpdateMembershipCardRequestDTO(String membershipCardName, Integer discountId) {
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
        this.isDefault = false;
    }
    
    public UpdateMembershipCardRequestDTO(String membershipCardName, Integer discountId, Boolean isDefault) {
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
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
}
