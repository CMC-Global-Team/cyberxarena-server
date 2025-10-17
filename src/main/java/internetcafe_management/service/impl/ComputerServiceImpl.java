package internetcafe_management.service.impl;

import internetcafe_management.dto.ComputerDTO;
import internetcafe_management.entity.Computer;
import internetcafe_management.entity.Computer.ComputerStatus;
import internetcafe_management.mapper.computer.ComputerMapper;
import internetcafe_management.repository.computer.ComputerRepository;
import internetcafe_management.service.computer.ComputerService;
import internetcafe_management.specification.ComputerSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    @Transactional(readOnly = true)
    public Page<ComputerDTO> getAllComputers(String name, String ip, String status, Pageable pageable) {
        Specification<Computer> spec = ComputerSpecification.search(name, ip, status);
        Page<Computer> computerPage = computerRepository.findAll(spec, pageable);
        return computerPage.map(computerMapper::toDto);
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
    @Override
    public ComputerDTO updateComputer(Integer id, ComputerDTO computerDTO) {
        Computer existingComputer = computerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy máy tính với ID: " + id));

        computerMapper.updateEntityFromDto(computerDTO, existingComputer);

        if (computerDTO.getStatus() != null) {
            try {
                existingComputer.setStatus(ComputerStatus.valueOf(computerDTO.getStatus()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Trạng thái không hợp lệ: " + computerDTO.getStatus());
            }
        }

        Computer updatedComputer = computerRepository.save(existingComputer);

        return computerMapper.toDto(updatedComputer);
    }

    @Override
    public void deleteComputer(Integer id) {
        if (!computerRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy máy tính với ID: " + id);
        }

        computerRepository.deleteById(id);
    }
}