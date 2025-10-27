package internetcafe_management.repository.revenue;

import internetcafe_management.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Integer> {
    boolean existsByDate(LocalDateTime date);
    Optional<Revenue> findByDate(LocalDateTime date);
}