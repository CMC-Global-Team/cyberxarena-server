package internetcafe_management.dto;

public class MembershipCardDTO {
    private Integer membershipCardId;
    private String membershipCardName;
    private Integer discountId;
    private String discountName;
    private String discountType;
    private Double discountValue;
    private Boolean isDefault;
    
    // Constructors
    public MembershipCardDTO() {}
    
    public MembershipCardDTO(Integer membershipCardId, String membershipCardName, Integer discountId, 
                           String discountName, String discountType, Double discountValue) {
        this.membershipCardId = membershipCardId;
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
        this.discountName = discountName;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.isDefault = false;
    }
    
    public MembershipCardDTO(Integer membershipCardId, String membershipCardName, Integer discountId, 
                           String discountName, String discountType, Double discountValue, Boolean isDefault) {
        this.membershipCardId = membershipCardId;
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
        this.discountName = discountName;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.isDefault = isDefault;
    }
    
    // Getters and Setters
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
    
    public Integer getDiscountId() {
        return discountId;
    }
    
    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }
    
    public String getDiscountName() {
        return discountName;
    }
    
    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }
    
    public String getDiscountType() {
        return discountType;
    }
    
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    
    public Double getDiscountValue() {
        return discountValue;
    }
    
    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}
