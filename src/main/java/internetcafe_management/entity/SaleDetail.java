package internetcafe_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "sale_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_detail_id")
    private Integer saleDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(name = "item_id", nullable = false)
    @Positive(message = "Mã mặt hàng phải là số dương")
    private Integer itemId;

    @Column(name = "quantity", nullable = false)
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}
