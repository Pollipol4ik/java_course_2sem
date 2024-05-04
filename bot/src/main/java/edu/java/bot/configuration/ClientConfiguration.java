package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class ClientConfiguration {

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient();
    }
}
