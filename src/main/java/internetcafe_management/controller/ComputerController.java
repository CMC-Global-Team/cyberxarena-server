package internetcafe_management.controller;

import internetcafe_management.dto.ComputerDTO;
import internetcafe_management.service.computer.ComputerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/computers")
public class ComputerController {

    private final ComputerService computerService;

    public ComputerController(ComputerService computerService) {
        this.computerService = computerService;
    }

    @PostMapping
    public ResponseEntity<ComputerDTO> createComputer(@Valid @RequestBody ComputerDTO computerDTO) {
        ComputerDTO createdComputer = computerService.createComputer(computerDTO);
        return new ResponseEntity<>(createdComputer, HttpStatus.CREATED);
    }
}