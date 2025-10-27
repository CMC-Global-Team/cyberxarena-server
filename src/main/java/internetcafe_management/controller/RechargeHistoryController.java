package internetcafe_management.controller;

import internetcafe_management.dto.CreateRechargeHistoryRequestDTO;
import internetcafe_management.dto.RechargeHistoryDTO;
import internetcafe_management.dto.RechargeHistorySearchRequestDTO;
import internetcafe_management.service.recharge_history.RechargeHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recharge-history")
@Tag(name = "Recharge History", description = "API for managing recharge history")
@CrossOrigin(origins = "*")
public class RechargeHistoryController {
    
    @Autowired
    private RechargeHistoryService rechargeHistoryService;
    
    @PostMapping
    @Operation(summary = "Create new recharge history", description = "Create a new recharge history record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recharge history created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    public ResponseEntity<RechargeHistoryDTO> createRechargeHistory(
            @Valid @RequestBody CreateRechargeHistoryRequestDTO request) {
        RechargeHistoryDTO createdRechargeHistory = rechargeHistoryService.createRechargeHistory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRechargeHistory);
    }
    
    @GetMapping("/{rechargeId}")
    @Operation(summary = "Get recharge history by ID", description = "Retrieve a specific recharge history record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recharge history found"),
            @ApiResponse(responseCode = "404", description = "Recharge history not found")
    })
    public ResponseEntity<RechargeHistoryDTO> getRechargeHistoryById(
            @Parameter(description = "Recharge history ID") @PathVariable Integer rechargeId) {
        RechargeHistoryDTO rechargeHistory = rechargeHistoryService.getRechargeHistoryById(rechargeId);
        return ResponseEntity.ok(rechargeHistory);
    }
    
    @GetMapping
    @Operation(summary = "Get all recharge history", description = "Retrieve all recharge history with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recharge history retrieved successfully")
    })
    public ResponseEntity<Page<RechargeHistoryDTO>> getAllRechargeHistory(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "rechargeDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RechargeHistoryDTO> rechargeHistoryPage = rechargeHistoryService.getAllRechargeHistory(pageable);
        return ResponseEntity.ok(rechargeHistoryPage);
    }
    
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get recharge history by customer ID", description = "Retrieve recharge history for a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recharge history retrieved successfully")
    })
    public ResponseEntity<List<RechargeHistoryDTO>> getRechargeHistoryByCustomerId(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId) {
        List<RechargeHistoryDTO> rechargeHistoryList = rechargeHistoryService.getRechargeHistoryByCustomerId(customerId);
        return ResponseEntity.ok(rechargeHistoryList);
    }
    
    @GetMapping("/customer/{customerId}/paged")
    @Operation(summary = "Get recharge history by customer ID with pagination", 
               description = "Retrieve recharge history for a specific customer with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recharge history retrieved successfully")
    })
    public ResponseEntity<Page<RechargeHistoryDTO>> getRechargeHistoryByCustomerIdPaged(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "rechargeDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RechargeHistoryDTO> rechargeHistoryPage = rechargeHistoryService.getRechargeHistoryByCustomerId(customerId, pageable);
        return ResponseEntity.ok(rechargeHistoryPage);
    }
    
    @PostMapping("/search")
    @Operation(summary = "Search recharge history", description = "Search recharge history with filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<Page<RechargeHistoryDTO>> searchRechargeHistory(
            @RequestBody RechargeHistorySearchRequestDTO searchRequest,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "rechargeDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<RechargeHistoryDTO> rechargeHistoryPage = rechargeHistoryService.searchRechargeHistory(searchRequest, pageable);
        return ResponseEntity.ok(rechargeHistoryPage);
    }
    
    @GetMapping("/customer/{customerId}/total")
    @Operation(summary = "Get total recharge amount for customer", 
               description = "Get total amount recharged by a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total amount retrieved successfully")
    })
    public ResponseEntity<Double> getTotalRechargeAmountByCustomerId(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId) {
        Double totalAmount = rechargeHistoryService.getTotalRechargeAmountByCustomerId(customerId);
        return ResponseEntity.ok(totalAmount);
    }
    
    @GetMapping("/customer/{customerId}/total/date-range")
    @Operation(summary = "Get total recharge amount for customer in date range", 
               description = "Get total amount recharged by a customer within a specific date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total amount retrieved successfully")
    })
    public ResponseEntity<Double> getTotalRechargeAmountByCustomerIdAndDateRange(
            @Parameter(description = "Customer ID") @PathVariable Integer customerId,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam String endDate) {
        Double totalAmount = rechargeHistoryService.getTotalRechargeAmountByCustomerIdAndDateRange(
                customerId, startDate, endDate);
        return ResponseEntity.ok(totalAmount);
    }
    
    @DeleteMapping("/{rechargeId}")
    @Operation(summary = "Delete recharge history", description = "Delete a recharge history record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recharge history deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Recharge history not found")
    })
    public ResponseEntity<Void> deleteRechargeHistory(
            @Parameter(description = "Recharge history ID") @PathVariable Integer rechargeId) {
        rechargeHistoryService.deleteRechargeHistory(rechargeId);
        return ResponseEntity.noContent().build();
    }
}
