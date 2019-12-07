package ros.hack.api.repository;

import ros.hack.api.entity.Operation;
import ros.hack.api.model.OperationsRequest;

import javax.annotation.Nonnull;
import java.util.List;

public interface OperationHistoryRepository {
    @Nonnull
    List<Operation> findAllOperations(@Nonnull OperationsRequest request);
}
