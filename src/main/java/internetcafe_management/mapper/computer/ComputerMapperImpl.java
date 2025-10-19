package internetcafe_management.mapper.computer;

import internetcafe_management.dto.ComputerDTO;
import internetcafe_management.entity.Computer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ComputerMapperImpl implements ComputerMapper {

    @Override
    public Computer toEntity(ComputerDTO computerDTO) {
        if (computerDTO == null) {
            return null;
        }

        Computer computer = new Computer();
        computer.setComputerId(computerDTO.getComputerId());
        computer.setComputerName(computerDTO.getComputerName());
        computer.setSpecifications(computerDTO.getSpecifications() != null ? 
            new HashMap<>(computerDTO.getSpecifications()) : new HashMap<>());
        computer.setIpAddress(computerDTO.getIpAddress());
        computer.setPricePerHour(computerDTO.getPricePerHour());
        computer.setStatus(mapStringToComputerStatus(computerDTO.getStatus()));

        return computer;
    }

    @Override
    public ComputerDTO toDto(Computer computer) {
        if (computer == null) {
            return null;
        }

        ComputerDTO computerDTO = new ComputerDTO();
        computerDTO.setComputerId(computer.getComputerId());
        computerDTO.setComputerName(computer.getComputerName());
        computerDTO.setSpecifications(computer.getSpecifications() != null ? 
            new HashMap<>(computer.getSpecifications()) : new HashMap<>());
        computerDTO.setIpAddress(computer.getIpAddress());
        computerDTO.setPricePerHour(computer.getPricePerHour());
        computerDTO.setStatus(mapComputerStatusToString(computer.getStatus()));

        return computerDTO;
    }

    @Override
    public void updateEntityFromDto(ComputerDTO dto, Computer entity) {
        if (dto == null) {
            return;
        }

        if (dto.getComputerId() != null) {
            entity.setComputerId(dto.getComputerId());
        }
        if (dto.getComputerName() != null) {
            entity.setComputerName(dto.getComputerName());
        }
        if (dto.getSpecifications() != null) {
            if (entity.getSpecifications() != null) {
                entity.getSpecifications().clear();
                entity.getSpecifications().putAll(dto.getSpecifications());
            } else {
                entity.setSpecifications(new HashMap<>(dto.getSpecifications()));
            }
        }
        if (dto.getIpAddress() != null) {
            entity.setIpAddress(dto.getIpAddress());
        }
        if (dto.getPricePerHour() != null) {
            entity.setPricePerHour(dto.getPricePerHour());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(mapStringToComputerStatus(dto.getStatus()));
        }
    }

    // Custom mapping methods for enum handling
    private Computer.ComputerStatus mapStringToComputerStatus(String status) {
        if (status == null) return null;
        return Computer.ComputerStatus.fromString(status);
    }

    private String mapComputerStatusToString(Computer.ComputerStatus status) {
        if (status == null) return null;
        return status.toJsonValue();
    }
}
