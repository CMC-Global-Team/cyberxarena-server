package internetcafe_management.dto;

import java.math.BigDecimal;

/**
 * DTO chứa thông tin về membership rank của khách hàng
 */
public class MembershipRankInfoDTO {
    
    private Integer customerId;
    private String customerName;
    private Integer currentMembershipCardId;
    private String currentMembershipCardName;
    private BigDecimal currentTotalRecharge;
    private BigDecimal currentBalance;
    private Integer nextMembershipCardId;
    private String nextMembershipCardName;
    private BigDecimal nextMembershipCardThreshold;
    private BigDecimal amountNeededForNextRank;
    
    // Constructors
    public MembershipRankInfoDTO() {}
    
    public MembershipRankInfoDTO(Integer customerId, String customerName, 
                                Integer currentMembershipCardId, String currentMembershipCardName,
                                BigDecimal currentTotalRecharge, BigDecimal currentBalance) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.currentMembershipCardId = currentMembershipCardId;
        this.currentMembershipCardName = currentMembershipCardName;
        this.currentTotalRecharge = currentTotalRecharge;
        this.currentBalance = currentBalance;
    }
    
    // Getters and Setters
    public Integer getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public Integer getCurrentMembershipCardId() {
        return currentMembershipCardId;
    }
    
    public void setCurrentMembershipCardId(Integer currentMembershipCardId) {
        this.currentMembershipCardId = currentMembershipCardId;
    }
    
    public String getCurrentMembershipCardName() {
        return currentMembershipCardName;
    }
    
    public void setCurrentMembershipCardName(String currentMembershipCardName) {
        this.currentMembershipCardName = currentMembershipCardName;
    }
    
    public BigDecimal getCurrentTotalRecharge() {
        return currentTotalRecharge;
    }
    
    public void setCurrentTotalRecharge(BigDecimal currentTotalRecharge) {
        this.currentTotalRecharge = currentTotalRecharge;
    }
    
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }
    
    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
    
    public Integer getNextMembershipCardId() {
        return nextMembershipCardId;
    }
    
    public void setNextMembershipCardId(Integer nextMembershipCardId) {
        this.nextMembershipCardId = nextMembershipCardId;
    }
    
    public String getNextMembershipCardName() {
        return nextMembershipCardName;
    }
    
    public void setNextMembershipCardName(String nextMembershipCardName) {
        this.nextMembershipCardName = nextMembershipCardName;
    }
    
    public BigDecimal getNextMembershipCardThreshold() {
        return nextMembershipCardThreshold;
    }
    
    public void setNextMembershipCardThreshold(BigDecimal nextMembershipCardThreshold) {
        this.nextMembershipCardThreshold = nextMembershipCardThreshold;
    }
    
    public BigDecimal getAmountNeededForNextRank() {
        return amountNeededForNextRank;
    }
    
    public void setAmountNeededForNextRank(BigDecimal amountNeededForNextRank) {
        this.amountNeededForNextRank = amountNeededForNextRank;
    }
}
