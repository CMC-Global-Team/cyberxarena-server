package internetcafe_management.dto;

import internetcafe_management.entity.Refund;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundDTO {

    private Integer refundId;

    @NotNull(message = "saleId không được để trống")
    private Integer saleId;

    @NotNull(message = "refundDate không được để trống")
    private LocalDateTime refundDate;

    @NotNull(message = "refundAmount không được để trống")
    @DecimalMin(value = "0.01", message = "Số tiền hoàn phải lớn hơn 0")
    @DecimalMax(value = "999999.99", message = "Số tiền hoàn không được vượt quá 999,999.99")
    private BigDecimal refundAmount;

    @Size(max = 200, message = "Lý do hoàn tiền không được vượt quá 200 ký tự")
    private String refundReason;

    @NotNull(message = "refundType không được để trống")
    private Refund.RefundType refundType;

    @Size(max = 100, message = "Người xử lý không được vượt quá 100 ký tự")
    private String processedBy;

    @NotNull(message = "status không được để trống")
    private Refund.RefundStatus status;

    // Danh sách chi tiết hoàn tiền
    private List<RefundDetailDTO> refundDetails;

    // Thông tin khách hàng (để hiển thị)
    private String customerName;
    private String customerPhone;

    // Thông tin hóa đơn gốc (để hiển thị)
    private LocalDateTime originalSaleDate;
    private BigDecimal originalSaleAmount;
}
