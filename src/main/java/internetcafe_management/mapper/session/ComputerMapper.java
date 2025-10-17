package internetcafe_management.mapper.session;

import internetcafe_management.dto.ComputerDTO;
import internetcafe_management.entity.Computer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComputerMapper {

    Computer toEntity(ComputerDTO computerDTO);

    ComputerDTO toDto(Computer computer);
}