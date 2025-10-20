package internetcafe_management.dto;

import internetcafe_management.entity.Discount.DiscountType;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {

    @NotBlank(message = "Discount name is required")
    @Size(max = 100, message = "Discount name must not exceed 100 characters")
    private String discount_name;

    @NotNull(message = "Discount type is required")
    private DiscountType discount_type;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Discount value must be greater than 0")
    private BigDecimal discount_value;
}


