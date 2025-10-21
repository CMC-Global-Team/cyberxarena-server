package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "session_usage_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private Integer usageId;

    @Column(name = "session_id", nullable = false)
    private Integer sessionId;

    @Column(name = "duration_hours")
    private Double durationHours;
}
