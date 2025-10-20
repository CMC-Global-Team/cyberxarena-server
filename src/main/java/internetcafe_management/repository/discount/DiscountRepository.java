package internetcafe_management.repository.discount;

import internetcafe_management.entity.Discount;
import internetcafe_management.entity.Discount.DiscountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findByDiscountType(DiscountType discountType);
}


