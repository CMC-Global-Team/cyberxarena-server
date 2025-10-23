package internetcafe_management.controller;

import internetcafe_management.dto.RefundDTO;
import internetcafe_management.entity.Refund;
import internetcafe_management.service.refund.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/refund")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    // Tạo refund mới
    @PostMapping
    public ResponseEntity<RefundDTO> createRefund(@RequestBody RefundDTO refundDTO) {
        try {
            RefundDTO createdRefund = refundService.createRefund(refundDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRefund);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Cập nhật refund
    @PutMapping("/{id}")
    public ResponseEntity<RefundDTO> updateRefund(@PathVariable Integer id, @RequestBody RefundDTO refundDTO) {
        try {
            RefundDTO updatedRefund = refundService.updateRefund(id, refundDTO);
            return ResponseEntity.ok(updatedRefund);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa refund
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefund(@PathVariable Integer id) {
        try {
            refundService.deleteRefund(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy refund theo ID
    @GetMapping("/{id}")
    public ResponseEntity<RefundDTO> getRefundById(@PathVariable Integer id) {
        try {
            RefundDTO refund = refundService.getRefundById(id);
            return ResponseEntity.ok(refund);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy tất cả refund với phân trang
    @GetMapping
    public ResponseEntity<Page<RefundDTO>> getAllRefunds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "refundId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<RefundDTO> refunds = refundService.getAllRefunds(pageable);
            return ResponseEntity.ok(refunds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Tìm kiếm refund với filter
    @GetMapping("/search")
    public ResponseEntity<Page<RefundDTO>> searchRefunds(
            @RequestParam(required = false) Refund.RefundStatus status,
            @RequestParam(required = false) String processedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "refundId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<RefundDTO> refunds = refundService.searchRefunds(status, processedBy, startDate, endDate, pageable);
            return ResponseEntity.ok(refunds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy refund theo sale ID
    @GetMapping("/sale/{saleId}")
    public ResponseEntity<List<RefundDTO>> getRefundsBySaleId(@PathVariable Integer saleId) {
        try {
            List<RefundDTO> refunds = refundService.getRefundsBySaleId(saleId);
            return ResponseEntity.ok(refunds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy refund theo customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<RefundDTO>> getRefundsByCustomerId(
            @PathVariable Integer customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "refundId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<RefundDTO> refunds = refundService.getRefundsByCustomerId(customerId, pageable);
            return ResponseEntity.ok(refunds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Cập nhật status của refund
    @PatchMapping("/{id}/status")
    public ResponseEntity<RefundDTO> updateRefundStatus(
            @PathVariable Integer id,
            @RequestParam Refund.RefundStatus status,
            @RequestParam(required = false) String processedBy) {
        try {
            RefundDTO updatedRefund = refundService.updateRefundStatus(id, status, processedBy);
            return ResponseEntity.ok(updatedRefund);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy thống kê refund theo status
    @GetMapping("/stats/count")
    public ResponseEntity<Long> getRefundCountByStatus(@RequestParam Refund.RefundStatus status) {
        try {
            long count = refundService.getRefundCountByStatus(status);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy tổng số tiền hoàn theo status
    @GetMapping("/stats/amount")
    public ResponseEntity<Double> getTotalRefundAmountByStatus(@RequestParam Refund.RefundStatus status) {
        try {
            Double totalAmount = refundService.getTotalRefundAmountByStatus(status);
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Kiểm tra xem sale đã có refund chưa
    @GetMapping("/check/sale/{saleId}")
    public ResponseEntity<Boolean> hasRefundForSale(@PathVariable Integer saleId) {
        try {
            boolean hasRefund = refundService.hasRefundForSale(saleId);
            return ResponseEntity.ok(hasRefund);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Lấy refund đang pending
    @GetMapping("/pending")
    public ResponseEntity<Page<RefundDTO>> getPendingRefunds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "refundId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<RefundDTO> refunds = refundService.getPendingRefunds(pageable);
            return ResponseEntity.ok(refunds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
