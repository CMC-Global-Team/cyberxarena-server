package internetcafe_management.mapper.sale;

import internetcafe_management.dto.CustomerDTO;
import internetcafe_management.dto.SaleDTO;
import internetcafe_management.dto.SaleItemDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.Sale;
import internetcafe_management.entity.SaleDetail;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SaleMapper {
    public SaleDTO toDTO(Sale entity) {
        if (entity == null) return null;
        SaleDTO dto = new SaleDTO();
        dto.setSaleId(entity.getSaleId());
        dto.setCustomerId(entity.getCustomer().getCustomerId());
        dto.setSaleDate(entity.getSaleDate());
        dto.setDiscountId(entity.getDiscountId());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setNote(entity.getNote());
        dto.setItems(entity.getSaleDetails() != null ? entity.getSaleDetails().stream()
                .map(detail -> new SaleItemDTO(detail.getItemId(), detail.getQuantity()))
                .collect(Collectors.toList()) : null);
        return dto;
    }


    public Sale toEntity(SaleDTO dto) {
        if (dto == null) return null;
        Sale entity = new Sale();
        entity.setSaleId(dto.getSaleId());
        entity.setCustomer(entity.getCustomer());
        entity.setSaleDate(dto.getSaleDate());
        entity.setDiscountId(dto.getDiscountId());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setNote(dto.getNote());
        entity.setSaleDetails(dto.getItems() != null ? dto.getItems().stream()
                .map(item -> {
                    SaleDetail detail = new SaleDetail();
                    detail.setItemId(item.getItemId());
                    detail.setQuantity(item.getQuantity());
                    detail.setSale(entity);
                    return detail;
                })
                .collect(Collectors.toList()) : null);
        return entity;
    }
}
