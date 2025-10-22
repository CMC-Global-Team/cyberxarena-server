package internetcafe_management.controller;

import internetcafe_management.dto.CreateMembershipCardRequestDTO;
import internetcafe_management.dto.MembershipCardDTO;
import internetcafe_management.dto.MembershipRankInfoDTO;
import internetcafe_management.dto.UpdateMembershipCardRequestDTO;
import internetcafe_management.service.MembershipCardService;
import internetcafe_management.service.MembershipRankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/membership-cards") // Fixed: removed duplicate /api prefix
@Tag(name = "Membership Card Management", description = "APIs for managing membership cards")
public class MembershipCardController {
    
    @Autowired
    private MembershipCardService membershipCardService;
    
    @Autowired
    private MembershipRankService membershipRankService;
    
    @PostMapping
    @Operation(summary = "Create a new membership card", description = "Create a new membership card with optional discount")
    public ResponseEntity<MembershipCardDTO> createMembershipCard(@Valid @RequestBody CreateMembershipCardRequestDTO request) {
        MembershipCardDTO membershipCard = membershipCardService.createMembershipCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(membershipCard);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get membership card by ID", description = "Retrieve a specific membership card by its ID")
    public ResponseEntity<MembershipCardDTO> getMembershipCardById(@PathVariable Integer id) {
        MembershipCardDTO membershipCard = membershipCardService.getMembershipCardById(id);
        return ResponseEntity.ok(membershipCard);
    }
    
    @GetMapping
    @Operation(summary = "Get all membership cards", description = "Retrieve all membership cards with their associated discounts")
    public ResponseEntity<List<MembershipCardDTO>> getAllMembershipCards() {
        List<MembershipCardDTO> membershipCards = membershipCardService.getAllMembershipCards();
        return ResponseEntity.ok(membershipCards);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update membership card", description = "Update an existing membership card")
    public ResponseEntity<MembershipCardDTO> updateMembershipCard(
            @PathVariable Integer id, 
            @Valid @RequestBody UpdateMembershipCardRequestDTO request) {
        MembershipCardDTO membershipCard = membershipCardService.updateMembershipCard(id, request);
        return ResponseEntity.ok(membershipCard);
    }
    
    @GetMapping("/{id}/usage")
    @Operation(summary = "Check membership card usage", description = "Check if membership card is being used by any customers")
    public ResponseEntity<Map<String, Object>> checkMembershipCardUsage(@PathVariable Integer id) {
        Map<String, Object> usageInfo = membershipCardService.checkMembershipCardUsage(id);
        return ResponseEntity.ok(usageInfo);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete membership card", description = "Delete a membership card by its ID")
    public ResponseEntity<Void> deleteMembershipCard(@PathVariable Integer id) {
        membershipCardService.deleteMembershipCard(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/default")
    @Operation(summary = "Get default membership card", description = "Retrieve the default membership card")
    public ResponseEntity<MembershipCardDTO> getDefaultMembershipCard() {
        MembershipCardDTO defaultCard = membershipCardService.getDefaultMembershipCard();
        return ResponseEntity.ok(defaultCard);
    }
    
    @GetMapping("/rank-info/{customerId}")
    @Operation(summary = "Get customer membership rank info", description = "Get detailed membership rank information for a customer")
    public ResponseEntity<MembershipRankInfoDTO> getCustomerMembershipRankInfo(@PathVariable Integer customerId) {
        MembershipRankInfoDTO rankInfo = membershipRankService.getMembershipRankInfo(customerId);
        return ResponseEntity.ok(rankInfo);
    }
    
    @PostMapping("/update-all-ranks")
    @Operation(summary = "Update all customers membership ranks", description = "Batch update membership ranks for all customers based on their recharge history")
    public ResponseEntity<String> updateAllCustomersMembershipRanks() {
        membershipRankService.updateAllCustomersMembershipRank();
        return ResponseEntity.ok("All customers membership ranks have been updated successfully");
    }
}
