package internetcafe-management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "customer_name", nullable = false, length = 100)
    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(min = 2, max = 100, message = "Tên khách hàng phải từ 2 đến 100 ký tự")
    private String customerName;

    @Column(name = "phone_number", length = 15)
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ (bắt đầu bằng 0 hoặc +84, gồm 10-11 số)")
    private String phoneNumber;

    @Column(name = "membership_card", length = 50)
    @NotBlank(message = "Loại thẻ không được để trống")
    @Size(max = 50, message = "Loại thẻ không vượt quá 50 ký tự")
    private String membershipCard;

    @Column(name = "balance", precision = 12, scale = 2, nullable = false)
    @NotNull(message = "Số dư không được để trống")
    @DecimalMin(value = "0.00", inclusive = true, message = "Số dư không được âm")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "registration_date", updatable = false, insertable = false)
    private LocalDateTime registrationDate;

}
