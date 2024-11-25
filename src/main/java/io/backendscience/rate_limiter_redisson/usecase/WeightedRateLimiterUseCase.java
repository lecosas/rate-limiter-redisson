package io.backendscience.rate_limiter_redisson.usecase;

import io.backendscience.rate_limiter_redisson.infrastructure.api.BasicController;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeightedRateLimiterUseCase {

    private final RedissonClient redissonClient;

    private RRateLimiter syncLimiter;
    private RRateLimiter asyncLimiter;

    private static final Logger logger = LoggerFactory.getLogger(BasicController.class);

    @PostConstruct
    public void init() {
        // Sync Rate Limiter (95% of TPS)
        this.syncLimiter = redissonClient.getRateLimiter("sync-limiter");
        this.syncLimiter.setRate(RateType.OVERALL, 10, Duration.ofSeconds(1));

        // Async Rate Limiter (5% of TPS)
        this.asyncLimiter = redissonClient.getRateLimiter("async-limiter");
        this.asyncLimiter.setRate(RateType.OVERALL, 1, Duration.ofSeconds(1));
    }

    public boolean acquireSyncPermission() {
        long startTime = System.currentTimeMillis();
        boolean acquired = syncLimiter.tryAcquire();
        logger.info("SYNC Acquires time: {}", System.currentTimeMillis() - startTime);
        return acquired;
    }

    public boolean acquireAsyncPermission() {
        long startTime = System.currentTimeMillis();
        boolean acquired = asyncLimiter.tryAcquire();
        logger.info("ASYNC Acquires time: {}", System.currentTimeMillis() - startTime);
        return acquired;
    }

    public boolean fallbackAcquireAsyncPermission() {
        logger.info("ASYNC FALLBACK ************************");
        return syncLimiter.tryAcquire();
    }
}
