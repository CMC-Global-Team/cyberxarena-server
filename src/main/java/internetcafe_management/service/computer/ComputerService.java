package internetcafe_management.service.computer;

import internetcafe_management.dto.ComputerDTO;
import internetcafe_management.dto.ComputerUsageStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComputerService {
    Page<ComputerDTO> getAllComputers(String name, String ip, String status, Pageable pageable);
    ComputerDTO createComputer(ComputerDTO computerDTO);
    ComputerDTO updateComputer(Integer id, ComputerDTO computerDTO);
    void deleteComputer(Integer id);
    ComputerUsageStats getComputerUsageStats(Integer id);
}