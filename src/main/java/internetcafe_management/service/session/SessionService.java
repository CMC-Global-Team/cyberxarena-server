package internetcafe_management.service.session;

import internetcafe_management.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SessionService {
    Page<Session> getAllSessions(Pageable pageable);
    Session createSession(Session session);
    Session updateSession(Integer id, Session session);
    void deleteSession(Integer id);
    List<Map<String, Object>> getSessionsWithTotalAmount();
    List<Session> searchSessions(Integer customerId, Integer computerId, LocalDateTime startTime, LocalDateTime endTime);
}
