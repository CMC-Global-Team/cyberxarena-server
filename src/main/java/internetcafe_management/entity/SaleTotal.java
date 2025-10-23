package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_total")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleTotal {
    @Id
    @Column(name = "sale_id")
    private Integer saleId;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    @MapsId
    private Sale sale;
}