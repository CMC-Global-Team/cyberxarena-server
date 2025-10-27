package internetcafe_management.controller;

import internetcafe_management.dto.SessionDetailsDTO;
import internetcafe_management.entity.Session;
import internetcafe_management.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    public Page<Session> getAllSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime,asc") String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        return sessionService.getAllSessions(pageable);
    }

    @PostMapping
    public Session createSession(@RequestBody Session session) {
        return sessionService.createSession(session);
    }

    @PutMapping("/{id}")
    public Session updateSession(@PathVariable Integer id, @RequestBody Session session) {
        return sessionService.updateSession(id, session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Integer id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public Page<Session> searchSessions(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String computerName,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "sessionId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        return sessionService.searchSessions(customerName, computerName, status, pageable);
    }

    @GetMapping("/with-details")
    public List<SessionDetailsDTO> getAllWithTotalAndUsage() {
        return sessionService.getSessionsWithTotalAmount();
    }

    @PostMapping("/{id}/end")
    public Session endSession(@PathVariable Integer id) {
        return sessionService.endSession(id);
    }

    @PutMapping("/{id}/change-computer")
    public Session changeComputer(@PathVariable Integer id, @RequestBody ChangeComputerRequest request) {
        return sessionService.changeComputer(id, request.getComputerId());
    }

    @GetMapping("/active")
    public List<Session> getActiveSessions() {
        return sessionService.getActiveSessions();
    }

    // Inner class for change computer request
    public static class ChangeComputerRequest {
        private Integer computerId;

        public Integer getComputerId() {
            return computerId;
        }

        public void setComputerId(Integer computerId) {
            this.computerId = computerId;
        }
    }
}
