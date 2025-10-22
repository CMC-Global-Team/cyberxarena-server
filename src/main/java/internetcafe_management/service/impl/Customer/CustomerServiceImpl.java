package internetcafe_management.service.impl.Customer;

import internetcafe_management.dto.CustomerDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.RechargeHistory;
import internetcafe_management.mapper.Customer.CustomerMapper;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.recharge_history.RechargeHistoryRepository;
import internetcafe_management.service.MembershipCardService;
import internetcafe_management.service.MembershipRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import internetcafe_management.service.Customer.CustomerService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    
    @Autowired
    private RechargeHistoryRepository rechargeHistoryRepository;
    
    @Autowired
    private MembershipRankService membershipRankService;


    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {
        System.out.println("Creating customer with DTO: " + dto); // Log DTO
        
        // Create new entity without setting customerId
        Customer entity = new Customer();
        entity.setCustomerName(dto.getCustomerName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setMembershipCardId(dto.getMembershipCardId());
        entity.setBalance(dto.getBalance());
        
        // If no membership card is specified (null or 0), try to set default membership card
        if (entity.getMembershipCardId() == null || entity.getMembershipCardId() == 0) {
            try {
                var defaultMembershipCard = membershipCardService.getDefaultMembershipCard();
                entity.setMembershipCardId(defaultMembershipCard.getMembershipCardId());
                System.out.println("Set default membership card ID: " + defaultMembershipCard.getMembershipCardId());
            } catch (Exception e) {
                // If no default membership card exists, leave it as null
                System.out.println("No default membership card found, leaving customer without membership card: " + e.getMessage());
                entity.setMembershipCardId(null);
            }
        }
        
        System.out.println("Entity before save: " + entity);
        Customer saved = customerRepository.save(entity);
        System.out.println("Entity after save: " + saved);
        
        // Nếu có số dư ban đầu > 0, tạo record trong lịch sử nạp tiền và cập nhật rank
        if (saved.getBalance() != null && saved.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            try {
                RechargeHistory initialRecharge = new RechargeHistory();
                initialRecharge.setCustomerId(saved.getCustomerId());
                initialRecharge.setAmount(saved.getBalance());
                initialRecharge.setRechargeDate(LocalDateTime.now());
                
                rechargeHistoryRepository.save(initialRecharge);
                System.out.println("✅ Created initial recharge history for customer " + saved.getCustomerId() + 
                                 " with amount: " + saved.getBalance());
                
                // Tự động cập nhật rank cho TẤT CẢ khách hàng khi có số dư ban đầu
                try {
                    // Tính tổng số tiền nạp của khách hàng (số dư ban đầu)
                    BigDecimal totalRecharge = customerRepository.getTotalRechargeAmountByCustomerId(saved.getCustomerId());
                    if (totalRecharge == null) {
                        totalRecharge = BigDecimal.ZERO;
                    }
                    
                    System.out.println("🔄 Customer " + saved.getCustomerId() + " total recharge (initial balance): " + totalRecharge);
                    System.out.println("🔄 Current membership card ID: " + saved.getMembershipCardId());
                    
                    membershipRankService.updateMembershipRank(saved.getCustomerId(), totalRecharge);
                    System.out.println("✅ Updated membership rank for customer " + saved.getCustomerId() + 
                                     " (auto-updated based on initial balance)");
                } catch (Exception rankError) {
                    System.err.println("❌ Error updating membership rank: " + rankError.getMessage());
                }
            } catch (Exception e) {
                System.err.println("❌ Error creating initial recharge history: " + e.getMessage());
                // Không throw exception để không ảnh hưởng đến việc tạo khách hàng
            }
        }
        
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
        
        // Lưu số dư cũ để so sánh
        BigDecimal oldBalance = existing.getBalance();
        
        existing.setCustomerName(dto.getCustomerName());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setMembershipCardId(dto.getMembershipCardId());
        existing.setBalance(dto.getBalance());
        
        Customer updated = customerRepository.save(existing);
        
        // Nếu số dư mới > số dư cũ, tạo record nạp tiền cho phần chênh lệch và cập nhật rank
        if (dto.getBalance() != null && oldBalance != null && 
            dto.getBalance().compareTo(oldBalance) > 0) {
            
            BigDecimal rechargeAmount = dto.getBalance().subtract(oldBalance);
            try {
                RechargeHistory rechargeHistory = new RechargeHistory();
                rechargeHistory.setCustomerId(customerId);
                rechargeHistory.setAmount(rechargeAmount);
                rechargeHistory.setRechargeDate(LocalDateTime.now());
                
                rechargeHistoryRepository.save(rechargeHistory);
                System.out.println("✅ Created recharge history for customer " + customerId + 
                                 " with additional amount: " + rechargeAmount);
                
                // Tự động cập nhật rank cho TẤT CẢ khách hàng khi cập nhật số dư
                try {
                    // Tính tổng số tiền nạp của khách hàng sau khi cập nhật
                    BigDecimal totalRecharge = customerRepository.getTotalRechargeAmountByCustomerId(customerId);
                    if (totalRecharge == null) {
                        totalRecharge = BigDecimal.ZERO;
                    }
                    
                    System.out.println("🔄 Customer " + customerId + " total recharge after balance update: " + totalRecharge);
                    System.out.println("🔄 Current membership card ID: " + updated.getMembershipCardId());
                    
                    membershipRankService.updateMembershipRank(customerId, totalRecharge);
                    System.out.println("✅ Updated membership rank for customer " + customerId + 
                                     " after balance update (auto-updated)");
                } catch (Exception rankError) {
                    System.err.println("❌ Error updating membership rank after balance update: " + rankError.getMessage());
                }
            } catch (Exception e) {
                System.err.println("❌ Error creating recharge history for balance update: " + e.getMessage());
            }
        }
        
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