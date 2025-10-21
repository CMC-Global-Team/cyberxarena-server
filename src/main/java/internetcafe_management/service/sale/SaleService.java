package internetcafe_management.service.sale;

import internetcafe_management.dto.SaleDTO;
import internetcafe_management.dto.UpdateSaleRequestDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.Sale;

public interface SaleService {
    Sale create(SaleDTO dto, Integer customerId);
    Sale update(Integer saleId, UpdateSaleRequestDTO dto);
    void delete(Integer saleId);
}
