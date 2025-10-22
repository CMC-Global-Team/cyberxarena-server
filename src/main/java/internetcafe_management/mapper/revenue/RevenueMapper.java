package internetcafe_management.mapper.revenue;

import internetcafe_management.dto.RevenueDTO;
import internetcafe_management.entity.Revenue;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RevenueMapper {

    RevenueDTO toDto(Revenue revenue);

    // để tính toán trường totalRevenue
    @AfterMapping
    default void calculateTotalRevenue(Revenue revenue, @MappingTarget RevenueDTO dto) {
        if (revenue.getComputerUsageRevenue() != null && revenue.getSalesRevenue() != null) {
            dto.setTotalRevenue(revenue.getComputerUsageRevenue().add(revenue.getSalesRevenue()));
        }
    }
}