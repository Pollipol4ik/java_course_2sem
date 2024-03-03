package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.github.GithubClient;
import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.client.stackoverflow.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public LinkInfoReceiver stackOverflowClient() {
        return new StackOverflowClient();
    }

    @Bean
    public LinkInfoReceiver githubClient() {
        return new GithubClient();
    }

    @Bean
    public BotClient botClient() {
        return new BotClient();
    }
}
