package internetcafe_management.controller;


import internetcafe_management.dto.SaleDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.Sale;
import internetcafe_management.service.sale.SaleService;
import internetcafe_management.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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
}
