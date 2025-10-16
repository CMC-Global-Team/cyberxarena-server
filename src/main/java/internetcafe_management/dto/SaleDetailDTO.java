package internetcafe_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailDTO {

    @NotNull(message = "Mã hóa đơn không được để trống")
    @Positive(message = "Mã hóa đơn phải là số dương")
    private Integer saleId;

    @NotNull(message = "Mã mặt hàng không được để trống")
    @Positive(message = "Mã mặt hàng phải là số dương")
    private Integer itemId;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}
