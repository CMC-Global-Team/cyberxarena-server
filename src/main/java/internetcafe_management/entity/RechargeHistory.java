package internetcafe_management.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recharge_history")
public class RechargeHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recharge_id")
    private Integer rechargeId;
    
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;
    
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "recharge_date")
    private LocalDateTime rechargeDate;
    
    // Relationship with Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;
    
    // Constructors
    public RechargeHistory() {}
    
    public RechargeHistory(Integer customerId, BigDecimal amount) {
        this.customerId = customerId;
        this.amount = amount;
        this.rechargeDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getRechargeId() {
        return rechargeId;
    }
    
    public void setRechargeId(Integer rechargeId) {
        this.rechargeId = rechargeId;
    }
    
    public Integer getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getRechargeDate() {
        return rechargeDate;
    }
    
    public void setRechargeDate(LocalDateTime rechargeDate) {
        this.rechargeDate = rechargeDate;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
