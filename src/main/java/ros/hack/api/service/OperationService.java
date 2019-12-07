package ros.hack.api.service;

import ros.hack.api.model.OperationInfo;
import ros.hack.api.model.OperationsRequest;

import javax.annotation.Nonnull;
import java.util.List;

public interface OperationService {
    @Nonnull
    List<OperationInfo> findOperations(@Nonnull OperationsRequest operationsRequest);
}
