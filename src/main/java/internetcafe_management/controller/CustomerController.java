package internetcafe_management.controller;

import internetcafe_management.dto.CustomerDTO;
import internetcafe_management.service.Customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO dto) { // Thêm @Valid để kích hoạt validation
        return ResponseEntity.ok(customerService.createCustomer(dto));
    }
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customerId) {
        System.out.println("Deleting customer with ID: " + customerId);
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Integer customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer customerId, @Valid @RequestBody CustomerDTO dto) { // Thêm @Valid
        return ResponseEntity.ok(customerService.updateCustomer(customerId, dto));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/sorted/asc")
    public ResponseEntity<List<CustomerDTO>> getAllSortedAsc() {
        return ResponseEntity.ok(customerService.getAllCustomersSortedAsc());
    }

    @GetMapping("/sorted/desc")
    public ResponseEntity<List<CustomerDTO>> getAllSortedDesc() {
        return ResponseEntity.ok(customerService.getAllCustomersSortedDesc());
    }

    @GetMapping("/sortedD/asc")
    public ResponseEntity<List<CustomerDTO>> getAllCustomersSoreDateAsc() {
        return ResponseEntity.ok(customerService.getAllCustomersSoreDateAsc());
    }
    @GetMapping("/sortedD/desc")
    public ResponseEntity<List<CustomerDTO>> getAllCustomersSortDateDesc() {
        return ResponseEntity.ok(customerService.getAllCustomersSortDateDesc());
    }
}