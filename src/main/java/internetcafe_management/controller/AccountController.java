package internetcafe_management.controller;

import internetcafe_management.dto.AccountDTO;
import internetcafe_management.dto.CreateAccountRequestDTO;
import internetcafe_management.dto.UpdateAccountRequestDTO;
import internetcafe_management.dto.AccountSearchRequestDTO;
import internetcafe_management.service.account.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
     * Tìm kiếm tài khoản với các bộ lọc và phân trang
     * @param username tên đăng nhập (optional)
     * @param customerName tên khách hàng (optional)
     * @param phoneNumber số điện thoại (optional)
     * @param membershipCard thẻ thành viên (optional)
     * @param page số trang (default: 0)
     * @param size kích thước trang (default: 10)
     * @param sortBy trường sắp xếp (default: accountId)
     * @param sortDirection hướng sắp xếp (default: asc)
     * @return ResponseEntity<Page<AccountDTO>>
     */
    @GetMapping("/search")
    public ResponseEntity<Page<AccountDTO>> searchAccounts(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String membershipCard,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "accountId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        AccountSearchRequestDTO searchRequest = new AccountSearchRequestDTO();
        searchRequest.setUsername(username);
        searchRequest.setCustomerName(customerName);
        searchRequest.setPhoneNumber(phoneNumber);
        searchRequest.setMembershipCard(membershipCard);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);
        
        Page<AccountDTO> result = accountService.searchAccounts(searchRequest);
        return ResponseEntity.ok(result);
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
     * Xóa tài khoản của khách hàng (hard delete)
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
}
