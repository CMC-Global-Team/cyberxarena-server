package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sale")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Integer saleId;

    // Khóa ngoại tới bảng customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @Column(name = "discount_id", nullable = false)
    private Integer discountId = 1;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "note", length = 200)
    private String note;

    // Quan hệ 1->N tới sale_detail
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleDetail> saleDetails;

    // Remove direct relationship to SaleTotal to avoid database constraint issues
    // SaleTotal will be managed separately

    @PrePersist
    protected void onCreate() {
        if (saleDate == null) {
            saleDate = LocalDateTime.now();
        }
        if (discountId == null) {
            discountId = 1;
        }
        // Don't create SaleTotal here - it needs saleId which is not available yet
    }
}