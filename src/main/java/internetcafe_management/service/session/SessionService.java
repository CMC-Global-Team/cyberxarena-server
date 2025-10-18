package internetcafe_management.service.session;

import internetcafe_management.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SessionService {
    Page<Session> getAllSessions(Pageable pageable);
    Session createSession(Session session);
    Session updateSession(Integer id, Session session);
    void deleteSession(Integer id);
}
