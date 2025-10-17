package internetcafe_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComputerDTO {

    private Integer computerId;

    @NotBlank(message = "Tên máy tính không được để trống")
    @Size(max = 50, message = "Tên máy tính không được vượt quá 50 ký tự")
    private String computerName;

    @NotNull(message = "Thông tin cấu hình không được để trống")
    private Map<String, Object> specifications;

    @NotBlank(message = "Địa chỉ IP không được để trống")
    @Pattern(
            regexp = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.|$)){4}$",
            message = "Địa chỉ IP không hợp lệ"
    )
    private String ipAddress;

    @NotNull(message = "Giá/giờ không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá/giờ phải lớn hơn 0")
    @Digits(integer = 10, fraction = 2, message = "Giá/giờ không hợp lệ")
    private BigDecimal pricePerHour;

    @NotBlank(message = "Trạng thái không được để trống")
    @Pattern(
            regexp = "Available|In Use|Broken",
            message = "Trạng thái chỉ được là: Available, In Use hoặc Broken"
    )
    private String status;
}
