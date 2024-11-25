package io.backendscience.rate_limiter_redisson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RateLimiterRedissonApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimiterRedissonApplication.class, args);
	}

}
