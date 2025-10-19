package internetcafe_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequestDTO {

    @NotBlank(message = "Tên mặt hàng không được để trống")
    @Size(min = 2, max = 100, message = "Tên mặt hàng phải từ 2-100 ký tự")
    private String itemName;

    @Size(max = 50, message = "Danh mục không vượt quá 50 ký tự")
    private String itemCategory;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.00", inclusive = false, message = "Giá phải lớn hơn 0")
    private java.math.BigDecimal price;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không được âm")
    private Integer stock;

    @Size(max = 100, message = "Tên nhà cung cấp không vượt quá 100 ký tự")
    private String supplierName;
}
