package internetcafe-management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @Column(name = "customer_name", nullable = false, length = 100)
    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(min = 2, max = 100, message = "Tên khách hàng phải từ 2-100 ký tự")
    private String customerName;

    @Column(name = "phone_number", length = 15)
    @Pattern(regexp = "\\d{10,15}", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @Column(name = "membership_card", length = 50)
    private String membershipCard;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @Column(name = "balance", precision = 12, scale = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "Số dư không được âm")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
        if (status == null) status = AccountStatus.ACTIVE;
    }

    public enum AccountStatus {
        ACTIVE("Đang hoạt động"),
        INACTIVE("Ngưng hoạt động");

        private final String displayName;
        AccountStatus(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }
}
