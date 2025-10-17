package internetcafe_management.service.impl.session;

import internetcafe_management.dto.SessionDTO;
import internetcafe_management.entity.Session;
import internetcafe_management.mapper.session.SessionMapper;
import internetcafe_management.repository.session.SessionRepository;
import internetcafe_management.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    @Override
    public Page<Session> getAllSessions(Pageable pageable, Optional<Integer> customerId, Optional<Integer> computerId) {
        Specification<Session> spec = Specification.where(null);

        if (customerId.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("customerId"), customerId.get()));
        }
        if (computerId.isPresent()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("computerId"), computerId.get()));
        }

        return sessionRepository.findAll(spec, pageable);
    }

    @Override
    public Session getSessionById(Integer id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiên sử dụng với ID: " + id));
    }

    @Override
    public Session createSession(SessionDTO sessionDTO) {
        if (sessionDTO.getEndTime() != null &&
                sessionDTO.getEndTime().isBefore(sessionDTO.getStartTime())) {
            throw new IllegalArgumentException("Giờ kết thúc không thể nhỏ hơn giờ bắt đầu");
        }
        Session session = sessionMapper.toEntity(sessionDTO);
        if (session.getStartTime() == null)
            session.setStartTime(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    @Override
    public Session updateSession(Integer id, SessionDTO sessionDTO) {
        Session existing = getSessionById(id);
        existing.setCustomerId(sessionDTO.getCustomerId());
        existing.setComputerId(sessionDTO.getComputerId());
        existing.setStartTime(sessionDTO.getStartTime());
        existing.setEndTime(sessionDTO.getEndTime());
        return sessionRepository.save(existing);
    }

    @Override
    public void deleteSession(Integer id) {
        Session session = getSessionById(id);
        sessionRepository.delete(session);
    }
}
