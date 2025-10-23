package internetcafe_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundDetailDTO {

    private Integer refundDetailId;

    @NotNull(message = "refundId không được để trống")
    private Integer refundId;

    @NotNull(message = "saleDetailId không được để trống")
    private Integer saleDetailId;

    @NotNull(message = "quantity không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    // Thông tin sản phẩm (để hiển thị)
    private String itemName;
    private BigDecimal itemPrice;
    private BigDecimal totalAmount;
}
