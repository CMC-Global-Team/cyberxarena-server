package internetcafe_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "Tên máy tính không được để trống")
    @Size(min = 2, max = 50, message = "Tên máy tính phải từ 2-50 ký tự")
    private String computerName;

    @Column(name = "status", nullable = false, length = 20)
    @NotBlank(message = "Trạng thái máy không được để trống")
    @Pattern(regexp = "^(Hoạt động|Bảo trì|Hỏng)$", message = "Trạng thái chỉ được là Hoạt động, Bảo trì hoặc Hỏng")
    private String status;

    @Column(name = "location", length = 100)
    @Size(max = 100, message = "Vị trí không vượt quá 100 ký tự")
    private String location;

    @Column(name = "last_used_time")
    private LocalDateTime lastUsedTime;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = "Hoạt động";
    }
}
