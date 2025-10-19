package internetcafe_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ComputerStatus status;

    public enum ComputerStatus {
        Available("Available"),
        In_Use("In Use"),
        Broken("Broken");

        private final String displayName;

        ComputerStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @JsonCreator
        public static ComputerStatus fromString(String value) {
            if (value == null) return null;
            
            // Handle database values with spaces
            switch (value.trim()) {
                case "Available":
                    return Available;
                case "In Use":
                    return In_Use;
                case "Broken":
                    return Broken;
                default:
                    // Try to match by enum name
                    try {
                        return ComputerStatus.valueOf(value.replace(" ", "_"));
                    } catch (IllegalArgumentException e) {
                        return Available; // Default fallback
                    }
            }
        }

        @JsonValue
        public String toJsonValue() {
            return displayName;
        }
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) status = ComputerStatus.Available;
    }
}
