package internetcafe_management.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateRechargeHistoryRequestDTO {
    
    @NotNull(message = "Customer ID is required")
    private Integer customerId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    // Constructors
    public CreateRechargeHistoryRequestDTO() {}
    
    public CreateRechargeHistoryRequestDTO(Integer customerId, BigDecimal amount) {
        this.customerId = customerId;
        this.amount = amount;
    }
    
    // Getters and Setters
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
}
