package ros.hack.api.redis;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ros.hack.api.entity.Operation;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class OperationCacheServiceImpl
        implements OperationCacheService {
    private static final Logger logger = getLogger(OperationCacheServiceImpl.class);

    private final RedisTemplate<String, Operation> redisTemplate;
    private final ListOperations<String, Operation> listOps;

    @Autowired
    public OperationCacheServiceImpl(RedisTemplate<String, Operation> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOps = redisTemplate.opsForList();
    }

    @Override
    @SuppressWarnings("all")
    public boolean contains(@Nonnull String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.warn("Failed to extract operations by key '{}'", key);
            return false;
        }
    }

    @Override
    public void save(@Nonnull String key, @Nonnull List<Operation> operations) {
        operations.forEach(result -> listOps.rightPush(key, result));
    }

    @Nonnull
    @Override
    @SuppressWarnings("all")
    public List<Operation> getValuesByKey(@Nonnull String key) {
        final List<Operation> results = newArrayList();
        while (listOps.size(key) > 0) {
            results.add(listOps.leftPop(key));
        }
        listOps.rightPushAll(key, results);
        return results;
    }
}
