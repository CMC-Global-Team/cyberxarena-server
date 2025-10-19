package internetcafe_management.controller;

import internetcafe_management.dto.AccountDTO;
import internetcafe_management.dto.CreateAccountRequestDTO;
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
     * Tạo tài khoản mới cho khách hàng
     * @param request thông tin tạo tài khoản
     * @return ResponseEntity<AccountDTO>
     */
    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountRequestDTO request) {
        try {
            AccountDTO account = accountService.createAccount(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
     * Kiểm tra username đã tồn tại chưa
     * @param username tên đăng nhập
     * @return ResponseEntity<Boolean>
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = accountService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Cập nhật thông tin tài khoản của khách hàng
     * @param customerId ID của khách hàng
     * @param request thông tin cập nhật
     * @return ResponseEntity<AccountDTO>
     */
    @PutMapping("/customer/{customerId}")
    public ResponseEntity<?> updateAccount(@PathVariable Integer customerId, 
                                         @Valid @RequestBody UpdateAccountRequestDTO request) {
        try {
            AccountDTO updatedAccount = accountService.updateAccount(customerId, request);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Xóa tài khoản của khách hàng (soft delete)
     * @param customerId ID của khách hàng
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Integer customerId) {
        try {
            accountService.deleteAccount(customerId);
            return ResponseEntity.ok("Account deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Vô hiệu hóa tài khoản của khách hàng
     * @param customerId ID của khách hàng
     * @return ResponseEntity<AccountDTO>
     */
    @PatchMapping("/customer/{customerId}/deactivate")
    public ResponseEntity<?> deactivateAccount(@PathVariable Integer customerId) {
        try {
            AccountDTO deactivatedAccount = accountService.deactivateAccount(customerId);
            return ResponseEntity.ok(deactivatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * Kích hoạt lại tài khoản của khách hàng
     * @param customerId ID của khách hàng
     * @return ResponseEntity<AccountDTO>
     */
    @PatchMapping("/customer/{customerId}/reactivate")
    public ResponseEntity<?> reactivateAccount(@PathVariable Integer customerId) {
        try {
            AccountDTO reactivatedAccount = accountService.reactivateAccount(customerId);
            return ResponseEntity.ok(reactivatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
