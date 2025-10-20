package internetcafe_management.controller;

import internetcafe_management.dto.DiscountDTO;
import internetcafe_management.entity.Discount;
import internetcafe_management.service.discount.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Discount Management", description = "APIs for managing discounts")
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping
    @Operation(summary = "Create new discount", description = "Create a new discount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Discount created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Discount> createDiscount(@Valid @RequestBody DiscountDTO discountDTO) {
        log.info("Received request to create discount");
        Discount created = discountService.createDiscount(discountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}


