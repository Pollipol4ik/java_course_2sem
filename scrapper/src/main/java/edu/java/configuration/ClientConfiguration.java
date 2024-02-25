package edu.java.configuration;

import edu.java.client.github.GithubClient;
import edu.java.client.link_information.LinkInformationReceiver;
import edu.java.client.stackoverflow.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public LinkInformationReceiver stackOverflowClient() {
        return new StackOverflowClient();
    }

    @Bean
    public LinkInformationReceiver githubClient() {
        return new GithubClient();
    }
}
