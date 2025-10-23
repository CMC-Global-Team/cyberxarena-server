package internetcafe_management.service.SaleDetail;

import internetcafe_management.dto.SaleDetailDTO;
import internetcafe_management.entity.SaleDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SaleDetailService {
    SaleDetailDTO create(SaleDetailDTO dto);


}
