package io.backendscience.rate_limiter_redisson.infrastructure.api;

import io.backendscience.rate_limiter_redisson.usecase.WeightedRateLimiterUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class BasicController {

    private final WeightedRateLimiterUseCase rateLimiterUseCase;

    private static final Logger logger = LoggerFactory.getLogger(BasicController.class);

    @GetMapping("/sync-process")
    public ResponseEntity<String> processSyncRequest() throws InterruptedException {
        //logger.info("Sync Process (RECEIVED)");

        if (!rateLimiterUseCase.acquireSyncPermission()) {
            logger.info("Sync Process request (TOO MANY REQUEST)");
            //Thread.sleep(1000);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("SYNC Rate limit exceeded");
        }

        logger.info("Sync Process request (SUCCESSFULLY)");
        return ResponseEntity.ok("SYNC Request processed successfully");
    }

    @GetMapping("/async-process")
    public ResponseEntity<String> processAsyncRequest() {
        //logger.info("Async Process (RECEIVED)");

        if (rateLimiterUseCase.acquireAsyncPermission() || rateLimiterUseCase.fallbackAcquireAsyncPermission()) {
//        if (rateLimiterUseCase.acquireAsyncPermission()) {
            logger.info("Async Process request (SUCCESSFULLY)");
            return ResponseEntity.ok("ASYNC Request processed successfully");
        }

        logger.info("Async Process request (TOO MANY REQUEST)");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("ASYNC Rate limit exceeded");
    }

}
