package ros.hack.api.redis;

import ros.hack.api.entity.Operation;

import javax.annotation.Nonnull;
import java.util.List;

public interface OperationCacheService {

    boolean contains(@Nonnull String key);

    void save(@Nonnull String key, @Nonnull List<Operation> operations);

    @Nonnull
    List<Operation> getValuesByKey(@Nonnull String key);
}
