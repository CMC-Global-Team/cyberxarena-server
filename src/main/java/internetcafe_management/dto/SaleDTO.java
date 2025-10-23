package internetcafe_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    private Integer saleId;

    @NotNull(message = "customerId không được để trống")
    private Integer customerId;

    @NotNull(message = "saleDate không được để trống")
    private LocalDateTime saleDate;

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    @Size(max = 50, message = "Phương thức thanh toán không được vượt quá 50 ký tự")
    private String paymentMethod;

    private BigDecimal totalAmount;
    @NotNull(message = "discountId không được để trống")
    private Integer discountId;

    @Size(max = 200, message = "Ghi chú không được vượt quá 200 ký tự")
    private String note;
}
