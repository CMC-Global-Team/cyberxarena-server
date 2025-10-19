package internetcafe_management.controller;

import internetcafe_management.dto.AccountDTO;
import internetcafe_management.dto.UpdateAccountRequestDTO;
import internetcafe_management.service.account.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountController {
    
    private final AccountService accountService;
    
    /**
     * Cập nhật thông tin tài khoản
     * @param accountId ID của tài khoản
     * @param request thông tin cập nhật
     * @return ResponseEntity<AccountDTO>
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<?> updateAccount(@PathVariable Integer accountId, 
                                         @Valid @RequestBody UpdateAccountRequestDTO request) {
        try {
            AccountDTO account = accountService.updateAccount(accountId, request);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Lấy thông tin tài khoản theo ID
     * @param accountId ID của tài khoản
     * @return ResponseEntity<AccountDTO>
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable Integer accountId) {
        try {
            AccountDTO account = accountService.findById(accountId);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Tìm tài khoản theo username
     * @param username tên đăng nhập
     * @return ResponseEntity<AccountDTO>
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getAccountByUsername(@PathVariable String username) {
        try {
            AccountDTO account = accountService.findByUsername(username);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Tìm tài khoản theo customer ID
     * @param customerId ID của khách hàng
     * @return ResponseEntity<AccountDTO>
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getAccountByCustomerId(@PathVariable Integer customerId) {
        try {
            AccountDTO account = accountService.findByCustomerId(customerId);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Kiểm tra username đã tồn tại chưa (trừ account hiện tại)
     * @param username tên đăng nhập
     * @param accountId ID của account hiện tại
     * @return ResponseEntity<Boolean>
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username,
                                                     @RequestParam(required = false) Integer accountId) {
        boolean exists;
        if (accountId != null) {
            exists = accountService.existsByUsernameAndAccountIdNot(username, accountId);
        } else {
            // Nếu không có accountId, kiểm tra tổng quát
            exists = accountService.findByUsername(username) != null;
        }
        return ResponseEntity.ok(exists);
    }
}
