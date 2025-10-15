package internetcafe_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "revenue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revenue_id")
    private Integer revenueId;

    @Column(name = "date", nullable = false)
    @NotNull(message = "Ngày không được để trống")
    private LocalDateTime date;

    @Column(name = "computer_usage_revenue", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Doanh thu từ máy tính không được để trống")
    @DecimalMin(value = "0.00", message = "Doanh thu không được âm")
    private BigDecimal computerUsageRevenue;

    @Column(name = "sales_revenue", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Doanh thu bán hàng không được để trống")
    @DecimalMin(value = "0.00", message = "Doanh thu không được âm")
    private BigDecimal salesRevenue;

    @Column(name = "operating_expense", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Chi phí vận hành không được để trống")
    @DecimalMin(value = "0.00", message = "Chi phí không được âm")
    private BigDecimal operatingExpense;

}
