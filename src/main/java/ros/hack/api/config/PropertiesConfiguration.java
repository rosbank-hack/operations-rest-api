package ros.hack.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ros.hack.api.config.properties.RedisProperties;

@Configuration
@EnableConfigurationProperties({
        RedisProperties.class
})
public class PropertiesConfiguration {
}
