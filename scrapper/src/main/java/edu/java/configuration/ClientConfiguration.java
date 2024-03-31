package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.github.events.EventProvider;
import edu.java.client.stackoverflow.StackOverflowClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Value("${client.stackoverflow.base-url}")
    private String stackOverflowBaseUrl;
    @Value("${client.github.base-url}")
    private String githubBaseUrl;
    @Value("${client.bot.base-url}")
    private String botBaseUrl;

    @Bean
    public WebClient webClient() {
        return (botBaseUrl.isEmpty()) ? WebClient.builder().baseUrl("http://localhost:8090").build()
            : WebClient.builder().baseUrl(botBaseUrl).build();
    }

    @Bean
    public GithubClient gitHubClient(List<EventProvider> eventProviderList) {
        if (githubBaseUrl == null || githubBaseUrl.isEmpty()) {
            return new GithubClient(eventProviderList);
        }
        return new GithubClient(githubBaseUrl, eventProviderList);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        if (stackOverflowBaseUrl == null || stackOverflowBaseUrl.isEmpty()) {
            return new StackOverflowClient();
        }
        return new StackOverflowClient(stackOverflowBaseUrl);
    }

}
