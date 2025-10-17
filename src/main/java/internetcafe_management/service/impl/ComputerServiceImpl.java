package internetcafe_management.service.impl;

import internetcafe_management.dto.ComputerDTO;
import internetcafe_management.entity.Computer;
import internetcafe_management.entity.Computer.ComputerStatus;
import internetcafe_management.mapper.computer.ComputerMapper;
import internetcafe_management.repository.computer.ComputerRepository;
import internetcafe_management.service.computer.ComputerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ComputerServiceImpl implements ComputerService {

    private final ComputerRepository computerRepository;
    private final ComputerMapper computerMapper;

    public ComputerServiceImpl(ComputerRepository repo, ComputerMapper mapper) {
        this.computerRepository = repo;
        this.computerMapper = mapper;
    }

    @Override
    public ComputerDTO createComputer(ComputerDTO computerDTO) {
        computerRepository.findByComputerName(computerDTO.getComputerName()).ifPresent(c -> {
            throw new IllegalArgumentException("Tên máy tính đã tồn tại.");
        });

        Computer computer = computerMapper.toEntity(computerDTO);

        try {
            computer.setStatus(ComputerStatus.valueOf(computerDTO.getStatus()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + computerDTO.getStatus());
        }

        Computer savedComputer = computerRepository.save(computer);

        return computerMapper.toDto(savedComputer);
    }
}