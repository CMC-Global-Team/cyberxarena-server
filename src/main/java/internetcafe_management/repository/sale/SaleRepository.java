package internetcafe_management.repository.sale;

import internetcafe_management.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    @Query("SELECT SUM(s.saleTotal.totalAmount) FROM Sale s " +
            "WHERE FUNCTION('DATE', s.saleDate) = :date")
    BigDecimal sumTotalAmountBySaleDate(@Param("date") LocalDate date);
    
    @Modifying
    @Query(value = "CALL update_sale_total(:saleId)", nativeQuery = true)
    void callUpdateSaleTotal(@Param("saleId") Integer saleId);
}
