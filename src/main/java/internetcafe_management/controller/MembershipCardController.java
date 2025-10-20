package internetcafe_management.controller;

import internetcafe_management.dto.CreateMembershipCardRequestDTO;
import internetcafe_management.dto.MembershipCardDTO;
import internetcafe_management.dto.UpdateMembershipCardRequestDTO;
import internetcafe_management.service.MembershipCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/membership-cards")
@Tag(name = "Membership Card Management", description = "APIs for managing membership cards")
public class MembershipCardController {
    
    @Autowired
    private MembershipCardService membershipCardService;
    
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
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete membership card", description = "Delete a membership card by its ID")
    public ResponseEntity<Void> deleteMembershipCard(@PathVariable Integer id) {
        membershipCardService.deleteMembershipCard(id);
        return ResponseEntity.noContent().build();
    }
}
