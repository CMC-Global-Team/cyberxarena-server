package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "computer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Computer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "computer_id")
    private Integer computerId;

    @Column(name = "computer_name", nullable = false, length = 50)
    private String computerName;

    @Column(name = "specifications", columnDefinition = "json", nullable = false)
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> specifications;

    @Column(name = "ip_address", nullable = false, length = 20)
    private String ipAddress;

    @Column(name = "price_per_hour", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = "Available";
    }
}
