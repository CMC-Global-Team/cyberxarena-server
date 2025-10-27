package internetcafe_management.dto;

import internetcafe_management.entity.Discount.DiscountType;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDiscountRequestDTO {

    @Size(max = 100, message = "Discount name must not exceed 100 characters")
    private String discount_name;

    private DiscountType discount_type;

    @DecimalMin(value = "0.00", inclusive = false, message = "Discount value must be greater than 0")
    private BigDecimal discount_value;
}


