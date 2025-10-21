package internetcafe_management.controller;

import internetcafe_management.dto.MembershipRankInfoDTO;
import internetcafe_management.entity.MembershipCard;
import internetcafe_management.service.MembershipRankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membership-ranks")
@Tag(name = "Membership Rank Management", description = "APIs for managing customer membership ranks")
public class MembershipRankController {
    
    @Autowired
    private MembershipRankService membershipRankService;
    
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get customer membership rank info", description = "Get detailed membership rank information for a specific customer")
    public ResponseEntity<MembershipRankInfoDTO> getCustomerMembershipRankInfo(@PathVariable Integer customerId) {
        MembershipRankInfoDTO rankInfo = membershipRankService.getMembershipRankInfo(customerId);
        return ResponseEntity.ok(rankInfo);
    }
    
    @GetMapping("/customer/{customerId}/current")
    @Operation(summary = "Get current membership card", description = "Get the current membership card for a customer")
    public ResponseEntity<MembershipCard> getCurrentMembershipCard(@PathVariable Integer customerId) {
        MembershipCard currentCard = membershipRankService.getCurrentMembershipCard(customerId);
        return ResponseEntity.ok(currentCard);
    }
    
    @GetMapping("/customer/{customerId}/next")
    @Operation(summary = "Get next membership card", description = "Get the next membership card that the customer can achieve")
    public ResponseEntity<MembershipCard> getNextMembershipCard(@PathVariable Integer customerId) {
        MembershipCard nextCard = membershipRankService.getNextMembershipCard(customerId);
        return ResponseEntity.ok(nextCard);
    }
    
    @PostMapping("/customer/{customerId}/update")
    @Operation(summary = "Update customer membership rank", description = "Manually update membership rank for a specific customer")
    public ResponseEntity<String> updateCustomerMembershipRank(@PathVariable Integer customerId) {
        membershipRankService.updateMembershipRank(customerId, null);
        return ResponseEntity.ok("Customer membership rank updated successfully");
    }
    
    @PostMapping("/update-all")
    @Operation(summary = "Update all customers membership ranks", description = "Batch update membership ranks for all customers")
    public ResponseEntity<String> updateAllCustomersMembershipRanks() {
        membershipRankService.updateAllCustomersMembershipRank();
        return ResponseEntity.ok("All customers membership ranks have been updated successfully");
    }
    
    @GetMapping("/debug/cards")
    @Operation(summary = "Debug membership cards", description = "Debug and display all membership cards information")
    public ResponseEntity<String> debugMembershipCards() {
        membershipRankService.debugMembershipCards();
        return ResponseEntity.ok("Debug information printed to console");
    }
}
