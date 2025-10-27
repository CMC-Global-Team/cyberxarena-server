package internetcafe_management.repository.sale;

import internetcafe_management.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    @Query(value = "SELECT SUM(st.total_amount) FROM sale s " +
            "JOIN sale_total st ON s.sale_id = st.sale_id " +
            "WHERE DATE(s.sale_date) = :date", nativeQuery = true)
    BigDecimal sumTotalAmountBySaleDate(@Param("date") LocalDate date);
    
    // Calculate sales revenue directly from sale_detail and item
    @Query(value = "SELECT COALESCE(SUM(sd.quantity * item.price), 0) " +
            "FROM sale s " +
            "JOIN sale_detail sd ON s.sale_id = sd.sale_id " +
            "JOIN item ON sd.item_id = item.item_id " +
            "WHERE DATE(s.sale_date) = :date", nativeQuery = true)
    BigDecimal calculateSalesRevenueByDate(@Param("date") LocalDate date);
    
    @Query(value = "SELECT COALESCE(SUM(rd.quantity * item.price), 0) FROM refund r " +
            "JOIN refund_detail rd ON r.refund_id = rd.refund_id " +
            "JOIN sale_detail sd ON rd.sale_detail_id = sd.sale_detail_id " +
            "JOIN item ON sd.item_id = item.item_id " +
            "JOIN sale s ON r.sale_id = s.sale_id " +
            "WHERE DATE(s.sale_date) = :date AND r.status IN ('Approved', 'Completed')", nativeQuery = true)
    BigDecimal sumRefundedAmountBySaleDate(@Param("date") LocalDate date);
    
    @Modifying
    @Transactional
    @Query(value = "CALL update_sale_total(:saleId)", nativeQuery = true)
    void callUpdateSaleTotal(@Param("saleId") Integer saleId);
}
