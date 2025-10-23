package internetcafe_management.mapper.sale;

import internetcafe_management.dto.SaleDTO;
import internetcafe_management.entity.Sale;
import internetcafe_management.entity.SaleTotal;
import internetcafe_management.mapper.Customer.CustomerMapper;
import internetcafe_management.service.Customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class SaleMapper {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    public SaleDTO toDTO(Sale entity) {
        if (entity == null) return null;
        SaleDTO dto = new SaleDTO();
        dto.setSaleId(entity.getSaleId());
        dto.setCustomerId(entity.getCustomer().getCustomerId());
        dto.setSaleDate(entity.getSaleDate());
        dto.setDiscountId(entity.getDiscountId());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setNote(entity.getNote());
        dto.setTotalAmount(dto.getTotalAmount() == null? new BigDecimal("0.00") : dto.getTotalAmount() );
        return dto;
    }
}