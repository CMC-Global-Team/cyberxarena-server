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
     * Kiểm tra username đã tồn tại chưa
     * @param username tên đăng nhập
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByUsername(String username);
    
    /**
     * Tìm account theo customer ID
     * @param customerId ID của khách hàng
     * @return Optional<Account>
     */
    Optional<Account> findByCustomerCustomerId(Integer customerId);
    
    /**
     * Kiểm tra customer đã có account chưa
     * @param customerId ID của khách hàng
     * @return true nếu đã có account, false nếu chưa
     */
    boolean existsByCustomerCustomerId(Integer customerId);
}
