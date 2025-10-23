package internetcafe_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputerUsageStats {
    private Integer computerId;
    private String computerName;
    private LocalDateTime lastUsed;
    private Double totalHours;
    private Integer totalSessions;
    private List<SessionUsageHistory> recentSessions;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionUsageHistory {
        private Integer sessionId;
        private String customerName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Double durationHours;
        private String status;
    }
}
