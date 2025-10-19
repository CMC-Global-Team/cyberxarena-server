package internetcafe_management.controller;

import internetcafe_management.dto.CustomerDTO;
import internetcafe_management.service.Customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO dto) { // Thêm @Valid để kích hoạt validation
        return ResponseEntity.ok(customerService.createCustomer(dto));
    }

}