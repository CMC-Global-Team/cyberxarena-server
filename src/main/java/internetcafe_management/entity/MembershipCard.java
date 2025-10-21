package internetcafe_management.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "membership_card")
public class MembershipCard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_card_id")
    private Integer membershipCardId;
    
    @Column(name = "membership_card_name", nullable = false, length = 100)
    private String membershipCardName;
    
    @Column(name = "discount_id")
    private Integer discountId;
    
    @Column(name = "recharge_threshold", precision = 12, scale = 2)
    private BigDecimal rechargeThreshold = BigDecimal.ZERO;
    
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", insertable = false, updatable = false)
    private Discount discount;
    
    // Constructors
    public MembershipCard() {}
    
    public MembershipCard(String membershipCardName, Integer discountId) {
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
        this.rechargeThreshold = BigDecimal.ZERO;
        this.isDefault = false;
    }
    
    public MembershipCard(String membershipCardName, Integer discountId, BigDecimal rechargeThreshold) {
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
        this.rechargeThreshold = rechargeThreshold != null ? rechargeThreshold : BigDecimal.ZERO;
        this.isDefault = false;
    }
    
    public MembershipCard(String membershipCardName, Integer discountId, BigDecimal rechargeThreshold, Boolean isDefault) {
        this.membershipCardName = membershipCardName;
        this.discountId = discountId;
        this.rechargeThreshold = rechargeThreshold != null ? rechargeThreshold : BigDecimal.ZERO;
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
    
    public Discount getDiscount() {
        return discount;
    }
    
    public void setDiscount(Discount discount) {
        this.discount = discount;
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
