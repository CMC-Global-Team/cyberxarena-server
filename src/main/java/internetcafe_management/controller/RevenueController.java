package internetcafe_management.controller;

import internetcafe_management.dto.RevenueDTO;
import internetcafe_management.service.revenue.RevenueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/revenue")
@Slf4j
public class RevenueController {

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
        log.info("🚀 RevenueController initialized successfully!");
        System.out.println("🚀 RevenueController initialized successfully!");
    }

    @GetMapping
    public ResponseEntity<Page<RevenueDTO>> getAllRevenue(Pageable pageable) {
        try {
            log.info("Getting all revenue with pageable: {}", pageable);
            Page<RevenueDTO> revenuePage = revenueService.getAllRevenue(pageable);
            log.info("Successfully retrieved {} revenue records", revenuePage.getTotalElements());
            return ResponseEntity.ok(revenuePage);
        } catch (Exception e) {
            log.error("Error getting all revenue: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testRevenueEndpoint() {
        try {
            log.info("Testing revenue endpoint");
            return ResponseEntity.ok("Revenue endpoint is working");
        } catch (Exception e) {
            log.error("Error testing revenue endpoint: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<List<RevenueDTO>> generateRevenueReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            log.info("Generating revenue reports from {} to {}", startDate, endDate);
            List<RevenueDTO> reports = revenueService.generateRevenueReports(startDate, endDate);
            log.info("Successfully generated {} revenue reports", reports.size());
            return new ResponseEntity<>(reports, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error generating revenue reports: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/recalculate")
    public ResponseEntity<RevenueDTO> recalculateRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            log.info("Recalculating revenue report for date: {}", date);
            RevenueDTO updatedReport = revenueService.recalculateRevenueReport(date);
            log.info("Successfully recalculated revenue report for {}", date);
            return ResponseEntity.ok(updatedReport);
        } catch (Exception e) {
            log.error("Error recalculating revenue report: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}