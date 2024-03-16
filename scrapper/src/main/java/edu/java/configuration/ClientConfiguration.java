package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.stackoverflow.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {

    @Value("${client.bot.base-url}")
    private String botBaseUrl;
    @Value("${client.github.base-url}")
    private String githubBaseUrl;
    @Value("${client.stackoverflow.base-url}")
    private String stackoverflowBaseUrl;


    @Bean
    public WebClient webClient() {
        return (botBaseUrl.isEmpty()) ? WebClient.builder().baseUrl("http://localhost:8090").build()
            : WebClient.builder().baseUrl(botBaseUrl).build();
    }

    @Bean
    public GithubClient gitHubInfoProvider() {
        if (githubBaseUrl == null || githubBaseUrl.isEmpty()) {
            return new GithubClient();
        }
        return new GithubClient(githubBaseUrl);
    }

    @Bean
    public StackOverflowClient stackOverflowInfoProvider() {
        if (stackoverflowBaseUrl == null || stackoverflowBaseUrl.isEmpty()) {
            return new StackOverflowClient();
        }
        return new StackOverflowClient(stackoverflowBaseUrl);
    }
}
