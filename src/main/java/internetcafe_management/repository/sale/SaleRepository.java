package internetcafe_management.repository.sale;

import internetcafe_management.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    @Query(value = "SELECT SUM(st.total_amount) FROM sale s " +
            "JOIN sale_total st ON s.sale_id = st.sale_id " +
            "WHERE DATE(s.sale_date) = :date", nativeQuery = true)
    BigDecimal sumTotalAmountBySaleDate(@Param("date") LocalDate date);
    
    @Modifying
    @Query(value = "CALL update_sale_total(:saleId)", nativeQuery = true)
    void callUpdateSaleTotal(@Param("saleId") Integer saleId);
}
