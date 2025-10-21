package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "session_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionUsage {

    @Id
    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "duration_hours")
    private Double durationHours;

    @Column(name = "remaining_hours")
    private Double remainingHours;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
}
