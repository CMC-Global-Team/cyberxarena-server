package internetcafe_management.service.impl.revenue;

import internetcafe_management.dto.RevenueDTO;
import internetcafe_management.entity.Revenue;
import internetcafe_management.mapper.revenue.RevenueMapper;
import internetcafe_management.repository.revenue.RevenueRepository;
import internetcafe_management.service.revenue.RevenueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RevenueServiceImpl implements RevenueService {

    private final RevenueRepository revenueRepository;
    private final RevenueMapper revenueMapper;

    public RevenueServiceImpl(RevenueRepository revenueRepository, RevenueMapper revenueMapper) {
        this.revenueRepository = revenueRepository;
        this.revenueMapper = revenueMapper;
    }

}