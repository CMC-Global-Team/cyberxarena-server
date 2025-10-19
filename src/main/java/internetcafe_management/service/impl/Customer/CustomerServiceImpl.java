package internetcafe_management.service.impl.Customer;

import internetcafe_management.dto.CustomerDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.mapper.Customer.CustomerMapper;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.Customer.CustomerSortedAscRepository;
import internetcafe_management.repository.Customer.CustomerSortedDescRepository;
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

    @Autowired
    private CustomerSortedAscRepository customerSortedAscRepository;

    @Autowired
    private CustomerSortedDescRepository customerSortedDescRepository;

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
    @Override
    public CustomerDTO updateCustomer(Integer customerId, CustomerDTO dto) {
        Customer existing = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        existing.setCustomerName(dto.getCustomerName());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setMembershipCard(dto.getMembershipCard());
        existing.setBalance(dto.getBalance());
        Customer updated = customerRepository.save(existing);
        return customerMapper.toDTO(updated);
    }
    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<CustomerDTO> getAllCustomersSortedAsc() {
        return customerSortedAscRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> getAllCustomersSortedDesc() {
        return customerSortedDescRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }
}