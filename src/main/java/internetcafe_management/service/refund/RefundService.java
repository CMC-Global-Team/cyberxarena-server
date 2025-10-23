package internetcafe_management.service.refund;

import internetcafe_management.dto.RefundDTO;
import internetcafe_management.entity.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface RefundService {
    
    // Tạo refund mới
    RefundDTO createRefund(RefundDTO refundDTO);
    
    // Cập nhật refund
    RefundDTO updateRefund(Integer refundId, RefundDTO refundDTO);
    
    // Xóa refund
    void deleteRefund(Integer refundId);
    
    // Lấy refund theo ID
    RefundDTO getRefundById(Integer refundId);
    
    // Lấy tất cả refund với phân trang
    Page<RefundDTO> getAllRefunds(Pageable pageable);
    
    // Tìm kiếm refund với filter
    Page<RefundDTO> searchRefunds(Refund.RefundStatus status, 
                                 String processedBy, 
                                 LocalDateTime startDate, 
                                 LocalDateTime endDate, 
                                 Pageable pageable);
    
    // Lấy refund theo sale ID
    List<RefundDTO> getRefundsBySaleId(Integer saleId);
    
    // Lấy refund theo customer ID
    Page<RefundDTO> getRefundsByCustomerId(Integer customerId, Pageable pageable);
    
    // Cập nhật status của refund
    RefundDTO updateRefundStatus(Integer refundId, Refund.RefundStatus status, String processedBy);
    
    // Lấy thống kê refund
    long getRefundCountByStatus(Refund.RefundStatus status);
    
    // Lấy tổng số tiền hoàn theo status
    Double getTotalRefundAmountByStatus(Refund.RefundStatus status);
    
    // Kiểm tra xem sale đã có refund chưa
    boolean hasRefundForSale(Integer saleId);
    
    // Lấy refund đang pending
    Page<RefundDTO> getPendingRefunds(Pageable pageable);
}
