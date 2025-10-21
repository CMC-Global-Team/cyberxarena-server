package internetcafe_management.mapper.recharge_history;

import internetcafe_management.dto.RechargeHistoryDTO;
import internetcafe_management.entity.RechargeHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RechargeHistoryMapper {
    
    RechargeHistoryMapper INSTANCE = Mappers.getMapper(RechargeHistoryMapper.class);
    
    @Mapping(source = "customer.customerName", target = "customerName", defaultExpression = "java(rechargeHistory.getCustomer() != null ? rechargeHistory.getCustomer().getCustomerName() : null)")
    RechargeHistoryDTO toDTO(RechargeHistory rechargeHistory);
    
    @Mapping(target = "customer", ignore = true)
    RechargeHistory toEntity(RechargeHistoryDTO rechargeHistoryDTO);
}
