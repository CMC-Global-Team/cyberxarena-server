package internetcafe_management.controller;

import internetcafe_management.dto.ComputerDTO;
import internetcafe_management.service.computer.ComputerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/computers")
@Tag(name = "Computer Management", description = "APIs for managing computers in the internet cafe")
public class ComputerController {

    private final ComputerService computerService;

    public ComputerController(ComputerService computerService) {
        this.computerService = computerService;
    }


    @GetMapping
    @Operation(summary = "Get all computers", description = "Retrieve a paginated list of computers with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved computers",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<Page<ComputerDTO>> getAllComputers(
            @Parameter(description = "Filter by computer name") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by IP address") @RequestParam(required = false) String ip,
            @Parameter(description = "Filter by computer status") @RequestParam(required = false) String status,
            @Parameter(description = "Pagination parameters") Pageable pageable) {

        Page<ComputerDTO> computers = computerService.getAllComputers(name, ip, status, pageable);
        return ResponseEntity.ok(computers);
    }

    @PostMapping
    @Operation(summary = "Create a new computer", description = "Add a new computer to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Computer created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ComputerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Computer already exists")
    })
    public ResponseEntity<ComputerDTO> createComputer(
            @Parameter(description = "Computer data to create") @Valid @RequestBody ComputerDTO computerDTO) {
        ComputerDTO createdComputer = computerService.createComputer(computerDTO);
        return new ResponseEntity<>(createdComputer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update computer", description = "Update an existing computer's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Computer updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ComputerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Computer not found")
    })
    public ResponseEntity<ComputerDTO> updateComputer(
            @Parameter(description = "Computer ID") @PathVariable Integer id,
            @Parameter(description = "Updated computer data") @Valid @RequestBody ComputerDTO computerDTO) {
        ComputerDTO updatedComputer = computerService.updateComputer(id, computerDTO);
        return ResponseEntity.ok(updatedComputer);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete computer", description = "Remove a computer from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Computer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Computer not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete computer - it may be in use")
    })
    public ResponseEntity<Void> deleteComputer(
            @Parameter(description = "Computer ID to delete") @PathVariable Integer id) {
        computerService.deleteComputer(id);
        return ResponseEntity.noContent().build();
    }
}