package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_sorted_date_asc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSortDateAsc {

    @Id
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "membership_card_id")
    private Integer membershipCardId;

    @Column(name = "balance", precision = 12, scale = 2)
    private BigDecimal balance;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
}