package internetcafe_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputerDTO {

    @NotBlank(message = "Tên máy tính không được để trống")
    @Size(min = 2, max = 50, message = "Tên máy tính phải từ 2-50 ký tự")
    private String computerName;

    @NotBlank(message = "Trạng thái máy không được để trống")
    @Pattern(regexp = "^(Hoạt động|Bảo trì|Hỏng)$", message = "Trạng thái chỉ được là Hoạt động, Bảo trì hoặc Hỏng")
    private String status;

    @Size(max = 100, message = "Vị trí không vượt quá 100 ký tự")
    private String location;

    private LocalDateTime lastUsedTime;
}
