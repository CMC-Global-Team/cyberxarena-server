package internetcafe_management.repository.recharge_history;

import internetcafe_management.entity.RechargeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RechargeHistoryRepository extends JpaRepository<RechargeHistory, Integer> {
    
    // Find by customer ID
    List<RechargeHistory> findByCustomerIdOrderByRechargeDateDesc(Integer customerId);
    
    // Find by customer ID with pagination
    Page<RechargeHistory> findByCustomerIdOrderByRechargeDateDesc(Integer customerId, Pageable pageable);
    
    // Find by date range
    List<RechargeHistory> findByRechargeDateBetweenOrderByRechargeDateDesc(
            LocalDateTime startDate, LocalDateTime endDate);
    
    // Find by customer ID and date range
    List<RechargeHistory> findByCustomerIdAndRechargeDateBetweenOrderByRechargeDateDesc(
            Integer customerId, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find with customer name using JOIN
    @Query("SELECT rh FROM RechargeHistory rh " +
           "JOIN rh.customer c " +
           "WHERE (:customerId IS NULL OR rh.customerId = :customerId) " +
           "AND (:startDate IS NULL OR rh.rechargeDate >= :startDate) " +
           "AND (:endDate IS NULL OR rh.rechargeDate <= :endDate) " +
           "AND (:customerName IS NULL OR LOWER(c.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))) " +
           "ORDER BY rh.rechargeDate DESC")
    Page<RechargeHistory> findWithFilters(@Param("customerId") Integer customerId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate,
                                         @Param("customerName") String customerName,
                                         Pageable pageable);
    
    // Get total recharge amount for a customer
    @Query("SELECT COALESCE(SUM(rh.amount), 0) FROM RechargeHistory rh WHERE rh.customerId = :customerId")
    Double getTotalRechargeAmountByCustomerId(@Param("customerId") Integer customerId);
    
    // Get total recharge amount for a customer in date range
    @Query("SELECT COALESCE(SUM(rh.amount), 0) FROM RechargeHistory rh " +
           "WHERE rh.customerId = :customerId " +
           "AND rh.rechargeDate BETWEEN :startDate AND :endDate")
    Double getTotalRechargeAmountByCustomerIdAndDateRange(@Param("customerId") Integer customerId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);
    
    // Get recharge history with customer details
    @Query("SELECT rh FROM RechargeHistory rh " +
           "JOIN FETCH rh.customer c " +
           "WHERE rh.customerId = :customerId " +
           "ORDER BY rh.rechargeDate DESC")
    List<RechargeHistory> findByCustomerIdWithCustomerDetails(@Param("customerId") Integer customerId);
}
