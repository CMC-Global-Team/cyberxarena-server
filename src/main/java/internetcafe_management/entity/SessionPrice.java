package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "session_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionPrice {

    @Id
    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;
}
