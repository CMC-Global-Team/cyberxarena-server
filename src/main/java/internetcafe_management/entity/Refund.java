package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "refund")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private Integer refundId;

    // Khóa ngoại tới bảng sale
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(name = "refund_date", nullable = false)
    private LocalDateTime refundDate;

    @Column(name = "refund_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "refund_reason", length = 200)
    private String refundReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_type", nullable = false)
    private RefundType refundType;

    @Column(name = "processed_by", length = 100)
    private String processedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RefundStatus status;

    // Quan hệ 1->N tới refund_detail
    @OneToMany(mappedBy = "refund", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefundDetail> refundDetails;

    @PrePersist
    protected void onCreate() {
        if (refundDate == null) {
            refundDate = LocalDateTime.now();
        }
        if (refundType == null) {
            refundType = RefundType.Full;
        }
        if (status == null) {
            status = RefundStatus.Pending;
        }
    }

    public enum RefundType {
        Full, Partial
    }

    public enum RefundStatus {
        Pending, Approved, Rejected, Completed
    }
}
