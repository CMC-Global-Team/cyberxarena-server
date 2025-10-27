package internetcafe_management.repository.discount;

import internetcafe_management.entity.Discount;
import internetcafe_management.entity.Discount.DiscountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findByDiscountType(DiscountType discountType);
    Optional<Discount> findByDiscountName(String discountName);
    
    @Query("SELECT mc.membershipCardId, mc.membershipCardName, mc.rechargeThreshold " +
           "FROM MembershipCard mc WHERE mc.discountId = :discountId")
    List<Object[]> findMembershipCardsUsingDiscount(@Param("discountId") Integer discountId);
}


