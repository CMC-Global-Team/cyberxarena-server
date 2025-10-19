package internetcafe_management.repository.Customer;

 import internetcafe_management.entity.CustomerSortedDesc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSortedDescRepository extends JpaRepository<CustomerSortedDesc, Integer> {
}