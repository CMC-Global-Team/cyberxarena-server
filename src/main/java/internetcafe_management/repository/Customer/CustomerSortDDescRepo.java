package internetcafe_management.repository.Customer;

import internetcafe_management.entity.CustomerSortDateDesc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSortDDescRepo extends JpaRepository<CustomerSortDateDesc, Integer> {
}