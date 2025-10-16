package internetcafe_management.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemDTO {
    @NotNull(message = "itemId không được để trống")
    private Integer itemId;

    @NotNull(message = "quantity không được để trống")
    @Min(value = 1, message = "Số lượng phải >= 1")
    private Integer quantity;
}
