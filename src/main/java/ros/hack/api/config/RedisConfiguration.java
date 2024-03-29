package ros.hack.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import ros.hack.api.config.properties.RedisProperties;
import ros.hack.api.entity.Operation;

@Configuration
@RequiredArgsConstructor
@ConditionalOnBean(PropertiesConfiguration.class)
public class RedisConfiguration {
    private final RedisProperties properties;

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(properties.getHost());
        jedisConnectionFactory.setPort(properties.getPort());
        jedisConnectionFactory.setPassword(properties.getPassword());
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Operation> redisTemplate() {
        RedisTemplate<String, Operation> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public Jedis jedis() {
        final JedisShardInfo shardInfo = new JedisShardInfo(properties.getHost(), properties.getPort());
        shardInfo.setPassword(properties.getPassword());
        final Jedis jedis = new Jedis(shardInfo);
        jedis.connect();
        return jedis;
    }
}
