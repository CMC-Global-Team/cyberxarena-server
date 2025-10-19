package internetcafe_management.service.account;

import internetcafe_management.dto.AccountDTO;
import internetcafe_management.dto.CreateAccountRequestDTO;
import internetcafe_management.dto.UpdateAccountRequestDTO;

public interface AccountService {
    
    /**
     * Tạo tài khoản mới cho khách hàng
     * @param request thông tin tạo tài khoản
     * @return AccountDTO thông tin tài khoản đã tạo
     */
    AccountDTO createAccount(CreateAccountRequestDTO request);
    
    /**
     * Tìm tài khoản theo username
     * @param username tên đăng nhập
     * @return AccountDTO thông tin tài khoản
     */
    AccountDTO findByUsername(String username);
    
    /**
     * Tìm tài khoản theo customer ID
     * @param customerId ID của khách hàng
     * @return AccountDTO thông tin tài khoản
     */
    AccountDTO findByCustomerId(Integer customerId);
    
    /**
     * Kiểm tra username đã tồn tại chưa
     * @param username tên đăng nhập
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByUsername(String username);
    
    /**
     * Cập nhật thông tin tài khoản
     * @param customerId ID của khách hàng
     * @param request thông tin cập nhật
     * @return AccountDTO thông tin tài khoản đã cập nhật
     */
    AccountDTO updateAccount(Integer customerId, UpdateAccountRequestDTO request);
    
    /**
     * Xóa tài khoản (soft delete - đặt isActive = false)
     * @param customerId ID của khách hàng
     */
    void deleteAccount(Integer customerId);
    
    /**
     * Vô hiệu hóa tài khoản
     * @param customerId ID của khách hàng
     * @return AccountDTO thông tin tài khoản đã vô hiệu hóa
     */
    AccountDTO deactivateAccount(Integer customerId);
    
    /**
     * Kích hoạt lại tài khoản
     * @param customerId ID của khách hàng
     * @return AccountDTO thông tin tài khoản đã kích hoạt
     */
    AccountDTO reactivateAccount(Integer customerId);
}
