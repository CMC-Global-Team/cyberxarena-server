package internetcafe_management.mapper.sale;

import internetcafe_management.dto.CustomerDTO;
import internetcafe_management.dto.SaleDTO;
import internetcafe_management.dto.SaleDetailDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.Sale;
import internetcafe_management.entity.SaleDetail;
import internetcafe_management.entity.SaleTotal;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
                .map(detail -> new SaleDetailDTO(detail.getSale().getSaleId(), detail.getItemId(), detail.getQuantity()))
                .collect(Collectors.toList()) : null);
        return dto;
    }


    public Sale toEntity(SaleDTO dto, Customer customer) {
        if (dto == null) {
            throw new IllegalArgumentException("SaleDTO cannot be null");
        }

        Sale entity = new Sale();
        entity.setCustomer(customer);
        entity.setSaleDate(dto.getSaleDate());
        entity.setDiscountId(dto.getDiscountId() != null ? dto.getDiscountId() : 1);
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setNote(dto.getNote());

        // Map SaleDetailDTO to SaleDetail - handle null items
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            entity.setSaleDetails(dto.getItems().stream()
                    .map(item -> {
                        SaleDetail detail = new SaleDetail();
                        detail.setItemId(item.getItemId());
                        detail.setQuantity(item.getQuantity());
                        detail.setSale(entity);
                        return detail;
                    })
                    .collect(Collectors.toList()));
        }

        // Note: SaleTotal is now managed separately, not through Sale entity

        return entity;
    }
}