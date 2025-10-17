package internetcafe_management.mapper.computer;

import internetcafe_management.dto.ComputerDTO;
import internetcafe_management.entity.Computer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ComputerMapper {

    Computer toEntity(ComputerDTO computerDTO);

    ComputerDTO toDto(Computer computer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ComputerDTO dto, @MappingTarget Computer entity);
}