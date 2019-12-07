package ros.hack.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ros.hack.api.entity.Operation;

public interface OperationRepository
        extends JpaRepository<Operation, Long> {
}
