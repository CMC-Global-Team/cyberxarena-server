package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "computer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Computer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer computerId;

    @Column(name = "computer_name", nullable = false, length = 50)
    private String computerName;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "last_used_time")
    private LocalDateTime lastUsedTime;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = "Hoạt động";
    }
}
