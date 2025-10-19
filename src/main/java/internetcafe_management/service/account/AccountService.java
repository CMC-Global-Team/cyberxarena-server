package internetcafe_management.service.account;

import internetcafe_management.dto.AccountDTO;
import internetcafe_management.dto.UpdateAccountRequestDTO;

public interface AccountService {
    
    /**
     * Cập nhật thông tin tài khoản
     * @param accountId ID của tài khoản
     * @param request thông tin cập nhật
     * @return AccountDTO thông tin tài khoản đã cập nhật
     */
    AccountDTO updateAccount(Integer accountId, UpdateAccountRequestDTO request);
    
    /**
     * Tìm tài khoản theo ID
     * @param accountId ID của tài khoản
     * @return AccountDTO thông tin tài khoản
     */
    AccountDTO findById(Integer accountId);
    
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
     * Kiểm tra username đã tồn tại chưa (trừ account hiện tại)
     * @param username tên đăng nhập
     * @param accountId ID của account hiện tại
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByUsernameAndAccountIdNot(String username, Integer accountId);
}
