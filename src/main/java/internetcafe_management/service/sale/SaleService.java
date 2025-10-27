package internetcafe_management.service.sale;

import internetcafe_management.dto.SaleDTO;
import internetcafe_management.dto.UpdateSaleRequestDTO;
import internetcafe_management.dto.UpdateSaleStatusDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.Sale;

import java.util.List;

public interface SaleService {
    SaleDTO create(SaleDTO dto, Integer customerId);
    SaleDTO update(Integer saleId, UpdateSaleRequestDTO dto);
    SaleDTO updateStatus(Integer saleId, UpdateSaleStatusDTO dto);
    void delete(Integer saleId);
    SaleDTO getSaleById(Integer saleId);
    List<SaleDTO> searchSale(String sortBy, String sortOrder, Integer saleId, Integer customerId, String customerName);
}
