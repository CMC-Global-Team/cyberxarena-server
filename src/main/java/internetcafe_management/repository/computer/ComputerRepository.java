package internetcafe_management.repository.computer;

import internetcafe_management.entity.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, Integer>, JpaSpecificationExecutor<Computer> {

    Optional<Computer> findByComputerName(String computerName);
    
    long countByStatus(String status);
}