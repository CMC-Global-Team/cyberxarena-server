package internetcafe_management.mapper.computer;

import internetcafe_management.dto.ComputerDTO;
import internetcafe_management.entity.Computer;

public interface ComputerMapper {

    Computer toEntity(ComputerDTO computerDTO);

    ComputerDTO toDto(Computer computer);

    void updateEntityFromDto(ComputerDTO dto, Computer entity);

    // Custom mapping methods for enum handling
    default Computer.ComputerStatus mapStringToComputerStatus(String status) {
        if (status == null) return null;
        return Computer.ComputerStatus.fromString(status);
    }

    default String mapComputerStatusToString(Computer.ComputerStatus status) {
        if (status == null) return null;
        return status.toJsonValue();
    }
}