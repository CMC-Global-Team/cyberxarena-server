package internetcafe_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sale")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer saleId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Khách hàng không được để trống")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Sản phẩm không được để trống")
    private Product product;

    @Column(name = "quantity", nullable = false)
    @Min(value = 1, message = "Số lượng phải ít nhất là 1")
    private Integer quantity;

    @Column(name = "total_price", precision = 12, scale = 2, nullable = false)
    @DecimalMin(value = "0.00", inclusive = false, message = "Tổng giá phải lớn hơn 0")
    private BigDecimal totalPrice;

    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    @PrePersist
    protected void onCreate() {
        saleDate = LocalDateTime.now();
    }
}
