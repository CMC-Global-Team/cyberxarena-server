package internetcafe_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountSearchRequestDTO {
    
    private String username;
    private String customerName;
    private String phoneNumber;
    private String membershipCard;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "accountId";
    private String sortDirection = "asc";
}
