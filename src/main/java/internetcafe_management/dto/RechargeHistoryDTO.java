package internetcafe_management.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RechargeHistoryDTO {
    
    private Integer rechargeId;
    private Integer customerId;
    private BigDecimal amount;
    private LocalDateTime rechargeDate;
    private String customerName; // For display purposes
    
    // Constructors
    public RechargeHistoryDTO() {}
    
    public RechargeHistoryDTO(Integer rechargeId, Integer customerId, BigDecimal amount, 
                             LocalDateTime rechargeDate, String customerName) {
        this.rechargeId = rechargeId;
        this.customerId = customerId;
        this.amount = amount;
        this.rechargeDate = rechargeDate;
        this.customerName = customerName;
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
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
