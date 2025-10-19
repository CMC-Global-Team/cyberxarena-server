package internetcafe_management.repository.Customer;

import internetcafe_management.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // Có thể thêm method tùy chỉnh, ví dụ:
    // List<Customer> findByCustomerNameContaining(String name);
}
