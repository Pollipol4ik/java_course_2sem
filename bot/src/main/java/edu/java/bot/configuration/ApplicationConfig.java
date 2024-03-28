package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public final class ApplicationConfig {
    private @NotEmpty String token;

}
