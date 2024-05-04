package edu.java.bot.controller.update_resolver;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.command.CommandChain;
import edu.java.bot.resolver.UpdateCallbackResolver;
import edu.java.bot.resolver.UpdateMessageResolver;
import edu.java.bot.resolver.UpdateResolver;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UpdateResolverTest {
    private static final MeterRegistry METER_REGISTRY = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    @Test
    @DisplayName("UpdateResolver#resolve with invalid Update test")
    public void test_shouldThrowException_whenUpdateIsInvalid() {

        UpdateResolver updateResolver =
            UpdateResolver.link(
                new UpdateMessageResolver(new CommandChain(), METER_REGISTRY),
                new UpdateCallbackResolver(new ScrapperClient())
            );

        Assertions.assertThatThrownBy(() -> updateResolver.resolve(new Update()))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("No next resolver available");
    }
}
