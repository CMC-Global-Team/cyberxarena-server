package internetcafe_management.service.impl.session;

import internetcafe_management.dto.SessionDetailsDTO;
import internetcafe_management.entity.Session;
import internetcafe_management.repository.session.SessionRepository;
import internetcafe_management.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public Session updateSession(Integer id, Session session) {
        Session existing = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        existing.setEndTime(session.getEndTime());
        return sessionRepository.save(existing);
    }

    @Override
    public void deleteSession(Integer id) {
        if (!sessionRepository.existsById(id))
            throw new RuntimeException("Session not found");
        sessionRepository.deleteById(id);
    }

    @Override
    public List<SessionDetailsDTO> getSessionsWithTotalAmount() {
        return sessionRepository.findAllWithDetails();
    }

    @Override
    public List<Session> searchSessions(Integer customerId, Integer computerId, LocalDateTime startTime, LocalDateTime endTime) {
        return sessionRepository.searchSessions(customerId, computerId, startTime, endTime);
    }
}
