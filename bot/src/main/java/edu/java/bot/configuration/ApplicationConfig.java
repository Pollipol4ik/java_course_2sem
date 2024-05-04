package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String token,
    Retry retry,
    @NotEmpty
    String topicName,
    boolean useQueue
) {
    @Bean
    public Retry retry() {
        return retry;
    }

    public record Retry(int maxAttempts, List<Class<? extends Throwable>> exceptions, Backoff backoff) {
        public record Backoff(Long delay, Long maxDelay, Double multiplier) {
        }
    }
}
