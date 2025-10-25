package internetcafe_management.repository.session;

import internetcafe_management.dto.SessionDetailsDTO;
import internetcafe_management.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    Page<Session> findAll(Pageable pageable);

    @Query("""
        SELECT new internetcafe_management.dto.SessionDetailsDTO(
            s.sessionId,
            s.customerId,
            s.computerId,
            s.startTime,
            s.endTime,
            sp.totalAmount,
            su.durationHours,
            su.remainingHours
        )
        FROM Session s
        LEFT JOIN SessionPrice sp ON s.sessionId = sp.sessionId
        LEFT JOIN SessionUsage su ON s.sessionId = su.sessionId
    """)
    List<SessionDetailsDTO> findAllWithDetails();

    @Query("""
        SELECT s FROM Session s
        WHERE (:customerId IS NULL OR s.customerId = :customerId)
        AND (:computerId IS NULL OR s.computerId = :computerId)
        AND (:startTime IS NULL OR s.startTime >= :startTime)
        AND (:endTime IS NULL OR s.endTime <= :endTime)
    """)
    List<Session> searchSessions(Integer customerId, Integer computerId,
                                 LocalDateTime startTime, LocalDateTime endTime);

    @Query("SELECT SUM(sp.totalAmount) FROM Session s " +
            "JOIN SessionPrice sp ON s.sessionId = sp.sessionId " +
            "WHERE FUNCTION('DATE', s.endTime) = :date")
    BigDecimal sumTotalAmountByEndDateTime(@Param("date") LocalDate date);

    List<Session> findByComputerIdOrderByStartTimeDesc(Integer computerId);
    
    @Query("SELECT COUNT(s) FROM Session s WHERE s.endTime IS NULL")
    long countActiveSessions();
    
    @Query("SELECT s FROM Session s WHERE s.endTime IS NULL")
    List<Session> findByEndTimeIsNull();
    
    @Query("SELECT s FROM Session s WHERE s.endTime IS NOT NULL ORDER BY s.startTime DESC")
    List<Session> findCompletedSessions();
    
    @Query("SELECT s FROM Session s ORDER BY s.startTime DESC")
    List<Session> findRecentSessions(Pageable pageable);
    
    List<Session> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
