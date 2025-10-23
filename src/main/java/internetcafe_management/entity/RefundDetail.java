package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refund_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_detail_id")
    private Integer refundDetailId;

    // Khóa ngoại tới bảng refund
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_id", nullable = false)
    private Refund refund;

    // Khóa ngoại tới bảng sale_detail
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_detail_id", nullable = false)
    private SaleDetail saleDetail;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
