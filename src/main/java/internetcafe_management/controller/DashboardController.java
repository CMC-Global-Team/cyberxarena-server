package internetcafe_management.controller;

import internetcafe_management.dto.DashboardStatsDTO;
import internetcafe_management.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard", description = "APIs for dashboard statistics and overview")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics", description = "Retrieve comprehensive dashboard statistics including computers, customers, revenue, and session data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved dashboard statistics"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        try {
            log.info("Getting dashboard statistics");
            DashboardStatsDTO stats = dashboardService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error getting dashboard statistics: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/recent-activities")
    @Operation(summary = "Get recent activities", description = "Retrieve recent system activities including logins, logouts, and recharges")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recent activities"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Map<String, Object>>> getRecentActivities(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            log.info("Getting recent activities with limit: {}", limit);
            List<Map<String, Object>> activities = dashboardService.getRecentActivities(limit);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            log.error("Error getting recent activities: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/computer-status")
    @Operation(summary = "Get computer status overview", description = "Retrieve computer status statistics including active, available, and maintenance computers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved computer status"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getComputerStatus() {
        try {
            log.info("Getting computer status overview");
            Map<String, Object> status = dashboardService.getComputerStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            log.error("Error getting computer status: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/revenue-trend")
    @Operation(summary = "Get revenue trend", description = "Retrieve revenue trend data for the last 7 days")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved revenue trend"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Map<String, Object>>> getRevenueTrend() {
        try {
            log.info("Getting revenue trend data");
            List<Map<String, Object>> trend = dashboardService.getRevenueTrend();
            return ResponseEntity.ok(trend);
        } catch (Exception e) {
            log.error("Error getting revenue trend: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
