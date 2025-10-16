package internetcafe_management.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTO {

    @NotNull(message = "Mã khách hàng không được để trống")
    @Positive(message = "Mã khách hàng phải là số dương")
    private Integer customerId;

    @NotNull(message = "Mã máy tính không được để trống")
    @Positive(message = "Mã máy tính phải là số dương")
    private Integer computerId;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @PastOrPresent(message = "Thời gian bắt đầu phải là trong quá khứ hoặc hiện tại")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
