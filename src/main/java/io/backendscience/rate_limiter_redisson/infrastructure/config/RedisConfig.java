package io.backendscience.rate_limiter_redisson.infrastructure.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Value("${redis-host}")
    public String redisHost;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
//                .setAddress("redis://localhost:6379")
                .setAddress(redisHost)
                .setTimeout(3000);
        return Redisson.create(config);
    }
}
