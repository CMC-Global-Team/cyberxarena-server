package internetcafe_management.repository.Customer;

import internetcafe_management.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
    /**
     * Tính tổng số tiền nạp của một khách hàng
     * @param customerId ID của khách hàng
     * @return Tổng số tiền nạp
     */
    @Query("SELECT COALESCE(SUM(rh.amount), 0) FROM RechargeHistory rh WHERE rh.customerId = :customerId")
    BigDecimal getTotalRechargeAmountByCustomerId(@Param("customerId") Integer customerId);
}
