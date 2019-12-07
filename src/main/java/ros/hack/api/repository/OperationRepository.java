package ros.hack.api.repository;

import ros.hack.api.entity.Operation;
import ros.hack.api.model.OperationsRequest;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface OperationRepository {
    @Nonnull
    List<Operation> findAll(@Nonnull OperationsRequest request);

    @Nonnull
    Optional<Operation> findById(@Nonnull Long id);
}
