package internetcafe_management.service.impl;

import internetcafe_management.dto.DashboardStatsDTO;
import internetcafe_management.entity.Computer;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.Session;
import internetcafe_management.repository.computer.ComputerRepository;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.session.SessionRepository;
import internetcafe_management.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final ComputerRepository computerRepository;
    private final CustomerRepository customerRepository;
    private final SessionRepository sessionRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        try {
            log.info("Calculating dashboard statistics");

            // Get computer statistics
            long totalComputers = computerRepository.count();
            long activeComputers = computerRepository.countByStatus("ACTIVE");
            long maintenanceComputers = computerRepository.countByStatus("MAINTENANCE");
            long availableComputers = totalComputers - activeComputers - maintenanceComputers;

            // Get customer statistics
            long totalCustomers = customerRepository.count();
            long onlineCustomers = sessionRepository.countActiveSessions();

            // Get today's revenue
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

            List<Session> todaySessions = sessionRepository.findByStartTimeBetween(startOfDay, endOfDay);
            BigDecimal todayRevenue = BigDecimal.ZERO; // Will be calculated from SessionPrice if needed

            int todayTransactions = todaySessions.size();

            // Calculate average session duration
            List<Session> completedSessions = sessionRepository.findCompletedSessions();
            double averageSessionDuration = completedSessions.stream()
                    .filter(session -> session.getStartTime() != null && session.getEndTime() != null)
                    .mapToDouble(session -> ChronoUnit.MINUTES.between(session.getStartTime(), session.getEndTime()))
                    .average()
                    .orElse(0.0) / 60.0; // Convert to hours

            // Calculate utilization rate
            String utilizationRate = totalComputers > 0 ? 
                    String.format("%.1f%%", (double) activeComputers / totalComputers * 100) : "0%";

            DashboardStatsDTO stats = new DashboardStatsDTO();
            stats.setTotalComputers(totalComputers);
            stats.setActiveComputers(activeComputers);
            stats.setTotalCustomers(totalCustomers);
            stats.setOnlineCustomers(onlineCustomers);
            stats.setTodayRevenue(todayRevenue);
            stats.setTodayTransactions(todayTransactions);
            stats.setAverageSessionDuration(averageSessionDuration);
            stats.setComputerUtilizationRate(utilizationRate);
            stats.setMaintenanceComputers(maintenanceComputers);
            stats.setAvailableComputers(availableComputers);
            return stats;

        } catch (Exception e) {
            log.error("Error calculating dashboard statistics: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to calculate dashboard statistics", e);
        }
    }

    @Override
    public List<Map<String, Object>> getRecentActivities(int limit) {
        try {
            log.info("Getting recent activities with limit: {}", limit);

            List<Map<String, Object>> activities = new ArrayList<>();

            // Get recent sessions (logins)
            Pageable pageable = PageRequest.of(0, limit);
            List<Session> recentSessions = sessionRepository.findRecentSessions(pageable);
            for (Session session : recentSessions) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("id", session.getSessionId());
                activity.put("computerName", "Máy #" + session.getComputerId());
                activity.put("action", "Khách hàng đăng nhập");
                activity.put("customerName", "Khách hàng #" + session.getCustomerId());
                activity.put("timestamp", session.getStartTime());
                activity.put("timeAgo", calculateTimeAgo(session.getStartTime()));
                activities.add(activity);
            }

            // Sort by timestamp descending
            activities.sort((a, b) -> {
                LocalDateTime timeA = (LocalDateTime) a.get("timestamp");
                LocalDateTime timeB = (LocalDateTime) b.get("timestamp");
                return timeB.compareTo(timeA);
            });

            return activities.stream().limit(limit).toList();

        } catch (Exception e) {
            log.error("Error getting recent activities: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get recent activities", e);
        }
    }

    @Override
    public Map<String, Object> getComputerStatus() {
        try {
            log.info("Getting computer status overview");

            long totalComputers = computerRepository.count();
            long activeComputers = computerRepository.countByStatus("ACTIVE");
            long maintenanceComputers = computerRepository.countByStatus("MAINTENANCE");
            long availableComputers = totalComputers - activeComputers - maintenanceComputers;

            String utilizationRate = totalComputers > 0 ? 
                    String.format("%.1f%%", (double) activeComputers / totalComputers * 100) : "0%";

            Map<String, Object> status = new HashMap<>();
            status.put("totalComputers", totalComputers);
            status.put("activeComputers", activeComputers);
            status.put("availableComputers", availableComputers);
            status.put("maintenanceComputers", maintenanceComputers);
            status.put("utilizationRate", utilizationRate);

            return status;

        } catch (Exception e) {
            log.error("Error getting computer status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get computer status", e);
        }
    }

    @Override
    public List<Map<String, Object>> getRevenueTrend() {
        try {
            log.info("Getting revenue trend data");

            List<Map<String, Object>> trend = new ArrayList<>();
            LocalDate today = LocalDate.now();

            for (int i = 6; i >= 0; i--) {
                LocalDate date = today.minusDays(i);
                LocalDateTime startOfDay = date.atStartOfDay();
                LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

                List<Session> daySessions = sessionRepository.findByStartTimeBetween(startOfDay, endOfDay);
                BigDecimal dayRevenue = BigDecimal.ZERO; // Will be calculated from SessionPrice if needed

                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", date.toString());
                dayData.put("revenue", dayRevenue);
                dayData.put("transactions", daySessions.size());
                trend.add(dayData);
            }

            return trend;

        } catch (Exception e) {
            log.error("Error getting revenue trend: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get revenue trend", e);
        }
    }

    private String calculateTimeAgo(LocalDateTime timestamp) {
        if (timestamp == null) return "Unknown";
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(timestamp, now);
        
        if (minutes < 1) return "Vừa xong";
        if (minutes < 60) return minutes + " phút trước";
        
        long hours = ChronoUnit.HOURS.between(timestamp, now);
        if (hours < 24) return hours + " giờ trước";
        
        long days = ChronoUnit.DAYS.between(timestamp, now);
        return days + " ngày trước";
    }
}
