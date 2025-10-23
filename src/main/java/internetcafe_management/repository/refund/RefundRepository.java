package internetcafe_management.repository.refund;

import internetcafe_management.entity.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer> {

    // Tìm kiếm refund theo saleId
    List<Refund> findBySaleSaleId(Integer saleId);

    // Tìm kiếm refund theo status
    Page<Refund> findByStatus(Refund.RefundStatus status, Pageable pageable);

    // Tìm kiếm refund theo processedBy
    Page<Refund> findByProcessedBy(String processedBy, Pageable pageable);

    // Tìm kiếm refund theo khoảng thời gian
    @Query("SELECT r FROM Refund r WHERE r.refundDate BETWEEN :startDate AND :endDate")
    Page<Refund> findByRefundDateBetween(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate, 
                                       Pageable pageable);

    // Tìm kiếm refund theo customer
    @Query("SELECT r FROM Refund r WHERE r.sale.customer.customerId = :customerId")
    Page<Refund> findByCustomerId(@Param("customerId") Integer customerId, Pageable pageable);

    // Tìm kiếm refund với filter tổng hợp
    @Query("SELECT r FROM Refund r WHERE " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:processedBy IS NULL OR r.processedBy = :processedBy) AND " +
           "(:startDate IS NULL OR r.refundDate >= :startDate) AND " +
           "(:endDate IS NULL OR r.refundDate <= :endDate)")
    Page<Refund> findWithFilters(@Param("status") Refund.RefundStatus status,
                                @Param("processedBy") String processedBy,
                                @Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate,
                                Pageable pageable);

    // Đếm số lượng refund theo status
    long countByStatus(Refund.RefundStatus status);

    // Tính tổng số tiền hoàn theo status
    @Query("SELECT SUM(r.refundAmount) FROM Refund r WHERE r.status = :status")
    Double sumRefundAmountByStatus(@Param("status") Refund.RefundStatus status);
}
