package internetcafe_management.service.dashboard;

import internetcafe_management.dto.DashboardStatsDTO;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    DashboardStatsDTO getDashboardStats();
    List<Map<String, Object>> getRecentActivities(int limit);
    Map<String, Object> getComputerStatus();
    List<Map<String, Object>> getRevenueTrend();
}
