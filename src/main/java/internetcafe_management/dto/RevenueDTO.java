package internetcafe_management.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueDTO {
    private Integer revenueId;
    private LocalDate date;
    private BigDecimal computerUsageRevenue;
    private BigDecimal salesRevenue;

    // Thêm một trường dẫn xuất (tính toán) để Frontend dễ hiển thị
    private BigDecimal totalRevenue;
    // Chúng ta sẽ tính toán totalRevenue trong Mapper
}