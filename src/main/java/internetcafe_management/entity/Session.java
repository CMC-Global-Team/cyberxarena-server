package internetcafe_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "customer_id", nullable = false)
    @NotNull(message = "Mã khách hàng không được để trống")
    private Integer customerId;

    @Column(name = "computer_id", nullable = false)
    @NotNull(message = "Mã máy tính không được để trống")
    private Integer computerId;

    @Column(name = "start_time", nullable = false)
    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

}
