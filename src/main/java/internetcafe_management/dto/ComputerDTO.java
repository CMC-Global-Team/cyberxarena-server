package internetcafe_management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Computer information")
public class ComputerDTO {

    @Schema(description = "Unique identifier for the computer", example = "1")
    private Integer computerId;

    @NotBlank(message = "Tên máy tính không được để trống")
    @Size(max = 50, message = "Tên máy tính không được vượt quá 50 ký tự")
    @Schema(description = "Name of the computer", example = "PC-001", maxLength = 50)
    private String computerName;

    @NotNull(message = "Thông tin cấu hình không được để trống")
    @Schema(description = "Computer specifications (CPU, RAM, GPU, etc.)", 
            example = "{\"cpu\": \"Intel i7-10700K\", \"ram\": \"16GB DDR4\", \"gpu\": \"RTX 3070\"}")
    private Map<String, Object> specifications;

    @NotBlank(message = "Địa chỉ IP không được để trống")
    @Pattern(
            regexp = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$",
            message = "Địa chỉ IP không hợp lệ"
    )
    @Schema(description = "IP address of the computer", example = "192.168.1.100")
    private String ipAddress;

    @NotNull(message = "Giá/giờ không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá/giờ phải lớn hơn 0")
    @Digits(integer = 10, fraction = 2, message = "Giá/giờ không hợp lệ")
    @Schema(description = "Price per hour for using this computer", example = "15000.00")
    private BigDecimal pricePerHour;

    @NotBlank(message = "Trạng thái không được để trống")
    @Pattern(
            regexp = "Available|In_Use|Broken",
            message = "Trạng thái chỉ được là: Available, In_Use hoặc Broken"
    )
    @Schema(description = "Current status of the computer", example = "Available", 
            allowableValues = {"Available", "In_Use", "Broken"})
    private String status;
}
