package internetcafe_management.service.impl.Customer;

import internetcafe_management.dto.CustomerDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.mapper.Customer.CustomerMapper;
import internetcafe_management.repository.Customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import internetcafe_management.service.Customer.CustomerService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {
        System.out.println("Creating customer with DTO: " + dto); // Log DTO
        Customer entity = customerMapper.toEntity(dto);
        Customer saved = customerRepository.save(entity);
        return customerMapper.toDTO(saved);
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        customerRepository.deleteById(customerId);
    }
    @Override
    public CustomerDTO getCustomerById(Integer customerId) {
        Customer entity = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        return customerMapper.toDTO(entity);
    }
}