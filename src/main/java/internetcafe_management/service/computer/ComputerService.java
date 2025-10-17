package internetcafe_management.service.computer;

import internetcafe_management.dto.ComputerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComputerService {
    Page<ComputerDTO> getAllComputers(Pageable pageable);
    ComputerDTO createComputer(ComputerDTO computerDTO);
    ComputerDTO updateComputer(Integer id, ComputerDTO computerDTO);
}