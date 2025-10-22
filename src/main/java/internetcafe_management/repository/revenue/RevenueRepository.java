package internetcafe_management.repository.revenue;

import internetcafe_management.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Integer> {
    boolean existsByDate(LocalDate date);
    Optional<Revenue> findByDate(LocalDate date);
}