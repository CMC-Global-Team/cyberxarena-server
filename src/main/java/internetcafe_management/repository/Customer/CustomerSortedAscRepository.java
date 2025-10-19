package internetcafe_management.repository.Customer;

import internetcafe_management.entity.CustomerSortedAsc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSortedAscRepository extends JpaRepository<CustomerSortedAsc, Integer> {
}