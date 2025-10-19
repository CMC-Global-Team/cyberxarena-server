package internetcafe_management.repository.account;

import internetcafe_management.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    /**
     * Tìm account theo username
     * @param username tên đăng nhập
     * @return Optional<Account>
     */
    Optional<Account> findByUsername(String username);
    
    /**
     * Kiểm tra username đã tồn tại chưa (trừ account hiện tại)
     * @param username tên đăng nhập
     * @param accountId ID của account hiện tại
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByUsernameAndAccountIdNot(String username, Integer accountId);
    
    /**
     * Tìm account theo customer ID
     * @param customerId ID của khách hàng
     * @return Optional<Account>
     */
    Optional<Account> findByCustomerCustomerId(Integer customerId);
}