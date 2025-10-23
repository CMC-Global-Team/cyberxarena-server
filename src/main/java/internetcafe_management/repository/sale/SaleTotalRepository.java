package internetcafe_management.repository.sale;

import internetcafe_management.entity.SaleTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleTotalRepository extends JpaRepository<SaleTotal, Integer> {
}
