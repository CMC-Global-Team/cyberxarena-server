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
    private LocalDateTime date;

    @Column(name = "computer_usage_revenue", nullable = false, precision = 10, scale = 2)
    private BigDecimal computerUsageRevenue;

    @Column(name = "sales_revenue", nullable = false, precision = 10, scale = 2)
    private BigDecimal salesRevenue;

    @Column(name = "operating_expense", nullable = false, precision = 10, scale = 2)
    private BigDecimal operatingExpense;

}
