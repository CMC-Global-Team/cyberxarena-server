package internetcafe_management.service.Customer;

import internetcafe_management.dto.CustomerDTO;


public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO dto);
    void deleteCustomer(Integer customerId);
    CustomerDTO getCustomerById(Integer customerId);
    CustomerDTO updateCustomer(Integer customerId, CustomerDTO dto);

}