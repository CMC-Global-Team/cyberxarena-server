package internetcafe_management.dto;

import java.time.LocalDateTime;

public class RechargeHistorySearchRequestDTO {
    
    private Integer customerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String customerName;
    
    // Constructors
    public RechargeHistorySearchRequestDTO() {}
    
    public RechargeHistorySearchRequestDTO(Integer customerId, LocalDateTime startDate, 
                                         LocalDateTime endDate, String customerName) {
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerName = customerName;
    }
    
    // Getters and Setters
    public Integer getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
