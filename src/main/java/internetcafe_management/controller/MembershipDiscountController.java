package internetcafe_management.controller;

import internetcafe_management.dto.DiscountCalculationDTO;
import internetcafe_management.entity.MembershipCard;
import internetcafe_management.service.MembershipDiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/membership-discounts")
@Tag(name = "Membership Discount Management", description = "APIs for calculating discounts based on membership cards")
public class MembershipDiscountController {
    
    @Autowired
    private MembershipDiscountService membershipDiscountService;
    
    @PostMapping("/calculate/{customerId}")
    @Operation(summary = "Calculate discount for customer", description = "Calculate discount amount for a customer based on their membership card")
    public ResponseEntity<BigDecimal> calculateDiscount(@PathVariable Integer customerId, 
                                                       @RequestParam BigDecimal amount) {
        BigDecimal discountAmount = membershipDiscountService.calculateDiscount(customerId, amount);
        return ResponseEntity.ok(discountAmount);
    }
    
    @PostMapping("/calculate-final/{customerId}")
    @Operation(summary = "Calculate final amount after discount", description = "Calculate the final amount a customer needs to pay after applying membership discount")
    public ResponseEntity<BigDecimal> calculateFinalAmount(@PathVariable Integer customerId, 
                                                          @RequestParam BigDecimal amount) {
        BigDecimal finalAmount = membershipDiscountService.calculateFinalAmount(customerId, amount);
        return ResponseEntity.ok(finalAmount);
    }
    
    @PostMapping("/calculate-details/{customerId}")
    @Operation(summary = "Calculate detailed discount information", description = "Get detailed discount calculation information for a customer")
    public ResponseEntity<DiscountCalculationDTO> calculateDiscountDetails(@PathVariable Integer customerId, 
                                                                          @RequestParam BigDecimal amount) {
        DiscountCalculationDTO discountDetails = membershipDiscountService.calculateDiscountDetails(customerId, amount);
        return ResponseEntity.ok(discountDetails);
    }
    
    @GetMapping("/info/{customerId}")
    @Operation(summary = "Get membership discount info", description = "Get the current membership card discount information for a customer")
    public ResponseEntity<MembershipCard> getMembershipDiscountInfo(@PathVariable Integer customerId) {
        MembershipCard discountInfo = membershipDiscountService.getMembershipDiscountInfo(customerId);
        return ResponseEntity.ok(discountInfo);
    }
}
