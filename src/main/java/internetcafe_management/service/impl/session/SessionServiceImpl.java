package internetcafe_management.service.impl.session;

import internetcafe_management.entity.Session;
import internetcafe_management.repository.session.SessionRepository;
import internetcafe_management.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public Page<Session> getAllSessions(Pageable pageable) {
        return sessionRepository.findAll(pageable);
    }
    @Override
    public Session createSession(Session session) {
        session.setEndTime(null);
        return sessionRepository.save(session);
    }
}
