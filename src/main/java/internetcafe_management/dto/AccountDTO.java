package internetcafe_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Integer accountId;
    private Integer customerId;
    private String username;
    private String customerName;
    private String phoneNumber;
    private String membershipCard;
}
