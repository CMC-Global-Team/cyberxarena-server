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

    @Column(name = "sale_id", nullable = false)
    @NotNull(message = "Mã hóa đơn không được để trống")
    private Integer saleId;

    @Column(name = "item_id", nullable = false)
    @NotNull(message = "Mã sản phẩm không được để trống")
    private Integer itemId;

    @Column(name = "quantity", nullable = false)
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}
