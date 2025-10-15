package internetcafe_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(name = "product_name", nullable = false, length = 100)
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 2, max = 100, message = "Tên sản phẩm phải từ 2-100 ký tự")
    private String productName;

    @Column(name = "category", length = 50)
    @Size(max = 50, message = "Danh mục không vượt quá 50 ký tự")
    private String category;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    @DecimalMin(value = "0.00", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    @Min(value = 0, message = "Số lượng không được âm")
    private Integer quantity;

    @Column(name = "status", length = 20)
    @Pattern(regexp = "^(Còn hàng|Hết hàng)$", message = "Trạng thái chỉ được là Còn hàng hoặc Hết hàng")
    private String status;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = "Còn hàng";
    }
}
