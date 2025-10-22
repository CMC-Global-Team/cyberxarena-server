package internetcafe_management.service.revenue;

import internetcafe_management.dto.RevenueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RevenueService {
    Page<RevenueDTO> getAllRevenue(Pageable pageable);
}