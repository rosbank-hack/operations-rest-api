package ros.hack.api.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ros.hack.api.entity.Operation;
import ros.hack.api.model.OperationInfo;
import ros.hack.api.model.OperationsRequest;
import ros.hack.api.redis.OperationCacheService;
import ros.hack.api.repository.OperationRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl
        implements OperationService {
    private static final Logger logger = getLogger(OperationServiceImpl.class);

    private final OperationRepository operationRepository;
    private final OperationCacheService operationCacheService;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<OperationInfo> findOperations(@Nonnull OperationsRequest request) {
        return loadOperations(request)
                .stream()
                .map(this::buildOperationInfo)
                .collect(toList());
    }

    @Nonnull
    private List<Operation> loadOperations(@Nonnull OperationsRequest request) {
        String requestHash = getQueryHash(request);
        if (operationCacheService.contains(requestHash)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Found operations in cache");
            }
            return operationCacheService.getValuesByKey(requestHash);
        }

        final List<Operation> operations = operationRepository.findAllOperations(request);
        operationCacheService.save(requestHash, operations);
        return operations;
    }

    @Nonnull
    private String getQueryHash(@Nonnull OperationsRequest request) {
        return UUID.randomUUID().toString(); // TODO
    }

    @Nonnull
    private OperationInfo buildOperationInfo(@Nonnull Operation operation) {
        return new OperationInfo(); // TODO
    }
}
