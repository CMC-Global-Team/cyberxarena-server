package internetcafe_management.repository.sale;

import internetcafe_management.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    @Query("SELECT SUM(st.total_amount) FROM SaleTotal st JOIN st.sale s " +
            "WHERE FUNCTION('DATE', s.sale_date) = :date")
    BigDecimal sumTotalAmountBySaleDate(@Param("date") LocalDate date);
}
