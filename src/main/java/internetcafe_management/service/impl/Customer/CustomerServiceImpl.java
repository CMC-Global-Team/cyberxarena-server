package internetcafe_management.service.impl.Customer;

import internetcafe_management.dto.CustomerDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.mapper.Customer.CustomerMapper;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.service.MembershipCardService;
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
    private MembershipCardService membershipCardService;


    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {
        System.out.println("Creating customer with DTO: " + dto); // Log DTO
        Customer entity = customerMapper.toEntity(dto);
        
        // If no membership card is specified, set default membership card
        if (entity.getMembershipCardId() == null) {
            try {
                var defaultMembershipCard = membershipCardService.getDefaultMembershipCard();
                entity.setMembershipCardId(defaultMembershipCard.getMembershipCardId());
            } catch (Exception e) {
                // If no default membership card exists, leave it as null
                System.out.println("No default membership card found, leaving customer without membership card");
            }
        }
        
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
        existing.setMembershipCardId(dto.getMembershipCardId());
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
    public List<CustomerDTO> searchCustomers(String sortBy, String sortOrder, String name, String phone, String email) {
        List<Customer> customers = customerRepository.findAll();
        
        // Filter by name if provided
        if (name != null && !name.trim().isEmpty()) {
            customers = customers.stream()
                    .filter(customer -> customer.getCustomerName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        // Filter by phone if provided
        if (phone != null && !phone.trim().isEmpty()) {
            customers = customers.stream()
                    .filter(customer -> customer.getPhoneNumber().contains(phone))
                    .collect(Collectors.toList());
        }
        
        // Filter by email if provided (assuming there's an email field)
        // Note: You may need to add email field to Customer entity if it doesn't exist
        
        // Sort by specified field and order
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            String order = (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) ? "desc" : "asc";
            
            switch (sortBy.toLowerCase()) {
                case "name":
                    customers = customers.stream()
                            .sorted((c1, c2) -> order.equals("desc") ? 
                                c2.getCustomerName().compareTo(c1.getCustomerName()) : 
                                c1.getCustomerName().compareTo(c2.getCustomerName()))
                            .collect(Collectors.toList());
                    break;
                case "date":
                    customers = customers.stream()
                            .sorted((c1, c2) -> order.equals("desc") ? 
                                c2.getRegistrationDate().compareTo(c1.getRegistrationDate()) : 
                                c1.getRegistrationDate().compareTo(c2.getRegistrationDate()))
                            .collect(Collectors.toList());
                    break;
                case "balance":
                    customers = customers.stream()
                            .sorted((c1, c2) -> order.equals("desc") ? 
                                c2.getBalance().compareTo(c1.getBalance()) : 
                                c1.getBalance().compareTo(c2.getBalance()))
                            .collect(Collectors.toList());
                    break;
                default:
                    // Default sort by name ascending
                    customers = customers.stream()
                            .sorted((c1, c2) -> c1.getCustomerName().compareTo(c2.getCustomerName()))
                            .collect(Collectors.toList());
                    break;
            }
        }
        
        return customers.stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }
}