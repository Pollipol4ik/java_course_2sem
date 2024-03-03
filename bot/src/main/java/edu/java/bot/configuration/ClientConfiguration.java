package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Value("${client.scrapper.base-url}")
    private String scrapperBaseUrl;

    @Bean
    public ScrapperClient scrapperClient() {
        if (!scrapperBaseUrl.isEmpty()) {
            return new ScrapperClient(scrapperBaseUrl);
        }
        return new ScrapperClient();
    }
}
