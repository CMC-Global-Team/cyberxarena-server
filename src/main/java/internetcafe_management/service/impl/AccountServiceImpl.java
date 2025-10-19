package internetcafe_management.service.impl;

import internetcafe_management.dto.AccountDTO;
import internetcafe_management.dto.UpdateAccountRequestDTO;
import internetcafe_management.entity.Account;
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
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public AccountDTO updateAccount(Integer accountId, UpdateAccountRequestDTO request) {
        // Tìm account hiện tại
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        
        // Kiểm tra current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        // Kiểm tra username đã tồn tại chưa (trừ account hiện tại)
        if (accountRepository.existsByUsernameAndAccountIdNot(request.getUsername(), accountId)) {
            throw new RuntimeException("Username already exists");
        }
        
        // Cập nhật username
        account.setUsername(request.getUsername());
        
        // Cập nhật password nếu có
        if (request.getNewPassword() != null && !request.getNewPassword().trim().isEmpty()) {
            account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        
        Account updatedAccount = accountRepository.save(account);
        
        return convertToDTO(updatedAccount);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AccountDTO findById(Integer accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        
        return convertToDTO(account);
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
    public boolean existsByUsernameAndAccountIdNot(String username, Integer accountId) {
        return accountRepository.existsByUsernameAndAccountIdNot(username, accountId);
    }
    
    private AccountDTO convertToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountId(account.getAccountId());
        dto.setCustomerId(account.getCustomer().getCustomerId());
        dto.setUsername(account.getUsername());
        dto.setCustomerName(account.getCustomer().getCustomerName());
        dto.setPhoneNumber(account.getCustomer().getPhoneNumber());
        dto.setMembershipCard(account.getCustomer().getMembershipCard());
        
        return dto;
    }
}
