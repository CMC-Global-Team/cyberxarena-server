package internetcafe_management.mapper.revenue;

import internetcafe_management.dto.RevenueDTO;
import internetcafe_management.entity.Revenue;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RevenueMapper {

    public RevenueMapper() {
        log.info("🚀 RevenueMapper initialized successfully!");
        System.out.println("🚀 RevenueMapper initialized successfully!");
    }

    public RevenueDTO toDto(Revenue revenue) {
        if (revenue == null) {
            return null;
        }
        
        log.debug("Mapping Revenue to DTO: {}", revenue.getRevenueId());
        
        RevenueDTO dto = new RevenueDTO();
        dto.setRevenueId(revenue.getRevenueId());
        dto.setDate(revenue.getDate().toLocalDate());
        dto.setComputerUsageRevenue(revenue.getComputerUsageRevenue());
        dto.setSalesRevenue(revenue.getSalesRevenue());
        
        // Tính toán totalRevenue
        if (revenue.getComputerUsageRevenue() != null && revenue.getSalesRevenue() != null) {
            dto.setTotalRevenue(revenue.getComputerUsageRevenue().add(revenue.getSalesRevenue()));
        }
        
        log.debug("Mapped Revenue to DTO: {}", dto.getRevenueId());
        return dto;
    }
}