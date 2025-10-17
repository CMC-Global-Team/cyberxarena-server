package internetcafe_management.service.session;

import internetcafe_management.dto.SessionDTO;
import internetcafe_management.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SessionService {
    Page<Session> getAllSessions(Pageable pageable, Optional<Integer> customerId, Optional<Integer> computerId);
    Session getSessionById(Integer id);
    Session createSession(SessionDTO sessionDTO);
    Session updateSession(Integer id, SessionDTO sessionDTO);
    void deleteSession(Integer id);
}
