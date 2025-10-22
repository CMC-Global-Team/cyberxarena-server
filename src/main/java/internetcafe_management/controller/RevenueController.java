package internetcafe_management.controller;

import internetcafe_management.dto.RevenueDTO;
import internetcafe_management.service.revenue.RevenueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/revenue")
public class RevenueController {

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping
    public ResponseEntity<Page<RevenueDTO>> getAllRevenue(Pageable pageable) {
        Page<RevenueDTO> revenuePage = revenueService.getAllRevenue(pageable);
        return ResponseEntity.ok(revenuePage);
    }

    @PostMapping("/generate")
    public ResponseEntity<List<RevenueDTO>> generateRevenueReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<RevenueDTO> reports = revenueService.generateRevenueReports(startDate, endDate);
        return new ResponseEntity<>(reports, HttpStatus.CREATED);
    }
}