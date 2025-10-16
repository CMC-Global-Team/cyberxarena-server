package internetcafe_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    @NotNull(message = "Khách hàng không được để trống")
    private Integer customerId;

    @NotNull(message = "Sản phẩm không được để trống")
    private Integer productId;

    @Min(value = 1, message = "Số lượng phải ít nhất là 1")
    private Integer quantity;

    @DecimalMin(value = "0.00", inclusive = false, message = "Tổng giá phải lớn hơn 0")
    private BigDecimal totalPrice;
}
