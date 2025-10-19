package internetcafe_management.service.impl;

import internetcafe_management.dto.AccountDTO;
import internetcafe_management.dto.CreateAccountRequestDTO;
import internetcafe_management.dto.UpdateAccountRequestDTO;
import internetcafe_management.dto.AccountSearchRequestDTO;
import internetcafe_management.entity.Account;
import internetcafe_management.entity.Customer;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.account.AccountRepository;
import internetcafe_management.service.account.AccountService;
import internetcafe_management.specification.AccountSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    
    @Override
    public AccountDTO updateAccount(Integer customerId, UpdateAccountRequestDTO request) {
        // Tìm account theo customer ID
        Account account = accountRepository.findByCustomerCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Account not found for customer ID: " + customerId));
        
        // Kiểm tra username mới có trùng với account khác không (nếu có thay đổi username)
        if (!account.getUsername().equals(request.getUsername())) {
            if (accountRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            account.setUsername(request.getUsername());
        }
        
        // Cập nhật password nếu có (chỉ khi password không null và không rỗng)
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        Account updatedAccount = accountRepository.save(account);
        
        return convertToDTO(updatedAccount);
    }
    
    @Override
    public void deleteAccount(Integer customerId) {
        // Tìm account theo customer ID
        Account account = accountRepository.findByCustomerCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Account not found for customer ID: " + customerId));
        
        // Hard delete - xóa hoàn toàn khỏi database
        accountRepository.delete(account);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AccountDTO> searchAccounts(AccountSearchRequestDTO searchRequest) {
        // Tạo Specification từ các filter
        Specification<Account> spec = AccountSpecification.search(
            searchRequest.getUsername(),
            searchRequest.getCustomerName(),
            searchRequest.getPhoneNumber(),
            searchRequest.getMembershipCard()
        );
        
        // Tạo Sort object
        Sort sort = Sort.by(
            "asc".equalsIgnoreCase(searchRequest.getSortDirection()) 
                ? Sort.Direction.ASC 
                : Sort.Direction.DESC,
            searchRequest.getSortBy()
        );
        
        // Tạo Pageable object
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);
        
        // Thực hiện tìm kiếm với phân trang
        Page<Account> accountPage = accountRepository.findAll(spec, pageable);
        
        // Chuyển đổi Page<Account> thành Page<AccountDTO>
        return accountPage.map(this::convertToDTO);
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
