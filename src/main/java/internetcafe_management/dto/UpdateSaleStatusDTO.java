package internetcafe_management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSaleStatusDTO {
    
    @NotBlank(message = "Trạng thái không được để trống")
    private String status; // Pending, Paid, Cancelled
}
