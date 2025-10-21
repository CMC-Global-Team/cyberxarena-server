package internetcafe_management.controller;


import internetcafe_management.dto.SaleDTO;
import internetcafe_management.dto.UpdateSaleRequestDTO;
import internetcafe_management.entity.Sale;
import internetcafe_management.service.sale.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sale")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @PostMapping
    public Sale createSession(@RequestBody SaleDTO sale, @RequestParam Integer customerId) {
        return saleService.create(sale, customerId);
    }

    @PutMapping("/{id}")
    public Sale updateSale(@PathVariable Integer id, @RequestBody UpdateSaleRequestDTO dto) {
        return saleService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComputer(@PathVariable Integer id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
