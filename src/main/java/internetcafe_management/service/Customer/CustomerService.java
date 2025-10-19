package internetcafe_management.service.Customer;

import internetcafe_management.dto.CustomerDTO;
import java.util.List;


public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO dto);
    void deleteCustomer(Integer customerId);
    CustomerDTO getCustomerById(Integer customerId);
    CustomerDTO updateCustomer(Integer customerId, CustomerDTO dto);
    List<CustomerDTO> getAllCustomers();
    List<CustomerDTO> getAllCustomersSortedAsc();
    List<CustomerDTO> getAllCustomersSortedDesc();
    List<CustomerDTO> getAllCustomersSoreDateAsc();
    List<CustomerDTO> getAllCustomersSortDateDesc();
}