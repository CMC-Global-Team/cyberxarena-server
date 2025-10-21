package internetcafe_management.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSaleRequestDTO {
    @Min(value = 0, message = "Mã khách hàng không được âm")
    private Integer customerId;

    private String paymentMethod;

    @Min(value = 0, message = "Mã giảm giá không được âm")
    private Integer discountId;

    @Size(max = 200, message = "Ghi chú không vượt quá 100 ký tự")
    private String note;
}
