package internetcafe_management.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "discount")
public class Discount {

    public enum DiscountType {
        Flat,
        Percentage
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Integer discountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType = DiscountType.Flat;

    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal discountValue;
    
    // Constructors
    public Discount() {}
    
    public Discount(DiscountType discountType, BigDecimal discountValue) {
        this.discountType = discountType;
        this.discountValue = discountValue;
    }
    
    // Getters and Setters
    public Integer getDiscountId() {
        return discountId;
    }
    
    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }
    
    public DiscountType getDiscountType() {
        return discountType;
    }
    
    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
    
    public BigDecimal getDiscountValue() {
        return discountValue;
    }
    
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
}


