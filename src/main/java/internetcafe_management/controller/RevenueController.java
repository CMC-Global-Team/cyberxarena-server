package internetcafe_management.controller;

import internetcafe_management.dto.RevenueDTO;
import internetcafe_management.service.revenue.RevenueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}