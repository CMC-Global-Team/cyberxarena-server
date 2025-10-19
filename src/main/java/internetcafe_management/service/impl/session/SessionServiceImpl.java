package internetcafe_management.service.impl.session;

import internetcafe_management.entity.Session;
import internetcafe_management.repository.session.SessionRepository;
import internetcafe_management.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> getSessionsWithTotalAmount() {
        List<Object[]> result = sessionRepository.findAllWithTotalAmount();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] row : result) {
            Session s = (Session) row[0];
            BigDecimal total = (BigDecimal) row[1];
            Map<String, Object> map = new HashMap<>();
            map.put("session", s);
            map.put("totalAmount", total);
            list.add(map);
        }
        return list;
    }
    @Override
    public List<Session> searchSessions(Integer customerId, Integer computerId, LocalDateTime startTime, LocalDateTime endTime) {
        return sessionRepository.searchSessions(customerId, computerId, startTime, endTime);
    }}
