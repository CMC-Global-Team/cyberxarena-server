package internetcafe_management.controller;


import internetcafe_management.dto.SaleDTO;
import internetcafe_management.dto.UpdateSaleRequestDTO;
import internetcafe_management.dto.UpdateSaleStatusDTO;
import internetcafe_management.entity.Sale;
import internetcafe_management.service.sale.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @PostMapping
    public SaleDTO createSale(@RequestBody SaleDTO sale, @RequestParam Integer customerId) {
        return saleService.create(sale, customerId);
    }

    @PutMapping("/{id}")
    public SaleDTO updateSale(@PathVariable Integer id, @RequestBody UpdateSaleRequestDTO dto) {
        return saleService.update(id, dto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SaleDTO> updateSaleStatus(@PathVariable Integer id, @RequestBody UpdateSaleStatusDTO dto) {
        SaleDTO updatedSale = saleService.updateStatus(id, dto);
        return ResponseEntity.ok(updatedSale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Integer id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Tìm kiếm sale theo điều kiện
    @GetMapping("/search")
    public ResponseEntity<List<SaleDTO>> searchSale(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) Integer saleId,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) String customerName) {
        return ResponseEntity.ok(saleService.searchSale(sortBy, sortOrder, saleId, customerId, customerName));
    }
}
