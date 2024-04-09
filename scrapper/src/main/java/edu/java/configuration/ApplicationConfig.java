package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    AccessType databaseAccessType,
    Retry retry,
    String topic,
    boolean useQueue
) {
    @Bean
    public Retry retry() {
        return retry;
    }

    @Bean
    public Scheduler scheduler() {
        return scheduler;
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Retry(int maxAttempts, List<Class<? extends Throwable>> exceptions, Backoff backoff) {
        public record Backoff(Long delay, Long maxDelay, Double multiplier) {
        }
    }
}
