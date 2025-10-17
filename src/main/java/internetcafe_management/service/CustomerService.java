package internetcafe_management.service;

import internetcafe_management.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO dto);
    CustomerDTO getCustomerById(Integer customerId);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO updateCustomer(Integer customerId, CustomerDTO dto);
    void deleteCustomer(Integer customerId);
}