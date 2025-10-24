package internetcafe_management.mapper.revenue;

import internetcafe_management.dto.RevenueDTO;
import internetcafe_management.entity.Revenue;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RevenueMapper {

    default RevenueDTO toDto(Revenue revenue) {
        if (revenue == null) {
            return null;
        }
        
        RevenueDTO dto = new RevenueDTO();
        dto.setRevenueId(revenue.getRevenueId());
        dto.setDate(revenue.getDate().toLocalDate());
        dto.setComputerUsageRevenue(revenue.getComputerUsageRevenue());
        dto.setSalesRevenue(revenue.getSalesRevenue());
        
        // Tính toán totalRevenue
        if (revenue.getComputerUsageRevenue() != null && revenue.getSalesRevenue() != null) {
            dto.setTotalRevenue(revenue.getComputerUsageRevenue().add(revenue.getSalesRevenue()));
        }
        
        return dto;
    }
}