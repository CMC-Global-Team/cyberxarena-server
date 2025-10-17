package internetcafe_management.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueDTO {

    @NotNull(message = "Mã doanh thu không được để trống")
    @Positive(message = "Mã doanh thu phải là số dương")
    private Integer revenueId;

    @NotNull(message = "Ngày không được để trống")
    @PastOrPresent(message = "Ngày phải là trong quá khứ hoặc hiện tại")
    private LocalDateTime date;

    @NotNull(message = "Doanh thu từ máy tính không được để trống")
    @DecimalMin(value = "0.00", inclusive = true, message = "Doanh thu từ máy tính không được âm")
    private BigDecimal computerUsageRevenue;

    @NotNull(message = "Doanh thu bán hàng không được để trống")
    @DecimalMin(value = "0.00", inclusive = true, message = "Doanh thu bán hàng không được âm")
    private BigDecimal salesRevenue;

    @NotNull(message = "Chi phí vận hành không được để trống")
    @DecimalMin(value = "0.00", inclusive = true, message = "Chi phí vận hành không được âm")
    private BigDecimal operatingExpense;
}
