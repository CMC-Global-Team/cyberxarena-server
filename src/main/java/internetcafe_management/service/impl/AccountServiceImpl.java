package internetcafe_management.service.impl;

import internetcafe_management.dto.AccountDTO;
import internetcafe_management.dto.CreateAccountRequestDTO;
import internetcafe_management.entity.Account;
import internetcafe_management.entity.Customer;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.account.AccountRepository;
import internetcafe_management.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {
    
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public AccountDTO createAccount(CreateAccountRequestDTO request) {
        // Kiểm tra customer có tồn tại không
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + request.getCustomerId()));
        
        // Kiểm tra customer đã có account chưa
        if (accountRepository.existsByCustomerCustomerId(request.getCustomerId())) {
            throw new RuntimeException("Customer already has an account");
        }
        
        // Kiểm tra username đã tồn tại chưa
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Tạo account mới
        Account account = new Account();
        account.setCustomer(customer);
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        
        Account savedAccount = accountRepository.save(account);
        
        return convertToDTO(savedAccount);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AccountDTO findByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found with username: " + username));
        
        return convertToDTO(account);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AccountDTO findByCustomerId(Integer customerId) {
        Account account = accountRepository.findByCustomerCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Account not found for customer ID: " + customerId));
        
        return convertToDTO(account);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }
    
    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountId(account.getAccountId());
        dto.setCustomerId(account.getCustomer().getCustomerId());
        dto.setUsername(account.getUsername());
        dto.setPassword(account.getPassword()); // Trong thực tế không nên trả về password
        dto.setCustomerName(account.getCustomer().getCustomerName());
        dto.setPhoneNumber(account.getCustomer().getPhoneNumber());
        dto.setMembershipCard(account.getCustomer().getMembershipCard());
        
        return dto;
    }
}
