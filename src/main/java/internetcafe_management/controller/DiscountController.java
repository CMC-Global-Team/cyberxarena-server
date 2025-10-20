package internetcafe_management.controller;

import internetcafe_management.dto.DiscountDTO;
import internetcafe_management.dto.UpdateDiscountRequestDTO;
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
import java.util.List;
import java.util.Optional;

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

    @GetMapping
    @Operation(summary = "Get all discounts", description = "Retrieve all discounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        log.info("Received request to get all discounts");
        List<Discount> discounts = discountService.getAllDiscounts();
        return ResponseEntity.ok(discounts);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update discount", description = "Update an existing discount by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Discount not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Discount> updateDiscount(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateDiscountRequestDTO updateRequestDTO) {
        log.info("Received request to update discount with id={}", id);
        Discount updated = discountService.updateDiscount(id, updateRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get discount by ID", description = "Retrieve a discount by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Discount not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Discount> getDiscountById(@PathVariable Integer id) {
        log.info("Received request to get discount by id={}", id);
        Optional<Discount> discountOpt = discountService.getDiscountById(id);
        return discountOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}


