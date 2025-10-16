package internetcafe_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(min = 2, max = 100, message = "Tên khách hàng phải từ 2 đến 100 ký tự")
    private String customerName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ (bắt đầu bằng 0 hoặc +84, gồm 10-11 số)")
    private String phoneNumber;

    @NotBlank(message = "Loại thẻ không được để trống")
    @Size(max = 50, message = "Loại thẻ không vượt quá 50 ký tự")
    private String membershipCard;

    @NotNull(message = "Số dư không được để trống")
    @DecimalMin(value = "0.00", inclusive = true, message = "Số dư không được âm")
    private BigDecimal balance;
}
