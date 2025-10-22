package internetcafe_management.service.revenue;

import internetcafe_management.dto.RevenueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface RevenueService {
    Page<RevenueDTO> getAllRevenue(Pageable pageable);
    List<RevenueDTO> generateRevenueReports(LocalDate startDate, LocalDate endDate);
}