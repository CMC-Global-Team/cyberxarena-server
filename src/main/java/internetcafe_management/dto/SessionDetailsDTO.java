package internetcafe_management.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SessionDetailsDTO {
    private Integer sessionId;
    private Integer customerId;
    private Integer computerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalAmount;
    private Double durationHours;

    public SessionDetailsDTO(Integer sessionId, Integer customerId, Integer computerId,
                             LocalDateTime startTime, LocalDateTime endTime,
                             BigDecimal totalAmount, Double durationHours) {
        this.sessionId = sessionId;
        this.customerId = customerId;
        this.computerId = computerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalAmount = totalAmount;
        this.durationHours = durationHours;
    }

    // Getters
    public Integer getSessionId() { return sessionId; }
    public Integer getCustomerId() { return customerId; }
    public Integer getComputerId() { return computerId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Double getDurationHours() { return durationHours; }
}
