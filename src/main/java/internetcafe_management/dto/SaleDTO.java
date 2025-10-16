package internetcafe_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    @NotNull(message = "customerId không được để trống")
    private Integer customerId;

    /**
     * Danh sách mặt hàng trong hóa đơn — mỗi phần tử chỉ cần itemId và quantity.
     */
    @NotEmpty(message = "Danh sách mặt hàng không được rỗng")
    private List<SaleItemDTO> items;

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    @Size(max = 50)
    private String paymentMethod;

    @NotNull(message = "discountType không được để trống")
    private String discountType;

    @DecimalMin(value = "0.00", inclusive = true, message = "Giảm giá không âm")
    private BigDecimal discount = BigDecimal.ZERO;

    @Size(max = 200)
    private String note;
}
