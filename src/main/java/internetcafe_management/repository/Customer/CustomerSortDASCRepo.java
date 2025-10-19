package internetcafe_management.repository.Customer;

import internetcafe_management.entity.CustomerSortDateAsc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSortDASCRepo extends JpaRepository<CustomerSortDateAsc, Integer> {
}