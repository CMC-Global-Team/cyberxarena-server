package internetcafe_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateMembershipCardRequestDTO {
    
    @NotBlank(message = "Membership card name is required")
    @Size(max = 100, message = "Membership card name must not exceed 100 characters")
    private String membershipCardName;
    
    private Integer discountId;
    
    // Constructors
    public CreateMembershipCardRequestDTO() {}
    
    public CreateMembershipCardRequestDTO(String membershipCardName, Integer discountId) {
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
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
}
