package internetcafe_management.controller;

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
@RequestMapping("/api/sessions")
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
    public ResponseEntity<Void> deleteComputer(@PathVariable Integer id) {
        sessionService.deleteSession(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public List<Session> searchSessions(
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) Integer computerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return sessionService.searchSessions(customerId, computerId, startTime, endTime);
    }

}
