package internetcafe_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalComputers;
    private Long activeComputers;
    private Long totalCustomers;
    private Long onlineCustomers;
    private BigDecimal todayRevenue;
    private Integer todayTransactions;
    private Double averageSessionDuration;
    private String computerUtilizationRate;
    private Long maintenanceComputers;
    private Long availableComputers;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class RecentActivityDTO {
    private Long id;
    private String computerName;
    private String action;
    private String customerName;
    private LocalDateTime timestamp;
    private String timeAgo;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ComputerStatusDTO {
    private Long totalComputers;
    private Long activeComputers;
    private Long availableComputers;
    private Long maintenanceComputers;
    private String utilizationRate;
}
