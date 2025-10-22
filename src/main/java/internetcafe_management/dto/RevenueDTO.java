package internetcafe_management.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RevenueDTO {
    private Integer revenueId;
    private LocalDate date;
    private BigDecimal computerUsageRevenue;
    private BigDecimal salesRevenue;

    // Thêm một trường dẫn xuất (tính toán) để Frontend dễ hiển thị
    private BigDecimal totalRevenue;
    // Chúng ta sẽ tính toán totalRevenue trong Mapper
}