package edu.java.configuration;

import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.repository.jpa.chat.JpaChatRepository;
import edu.java.repository.jpa.link.JpaLinkRepository;
import edu.java.service.chat.ChatService;
import edu.java.service.chat.JpaChatService;
import edu.java.service.link.JpaLinkService;
import edu.java.service.link.LinkService;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaConfiguration {

    @Bean
    public ChatService jpaChatService(JpaChatRepository chatRepository) {
        return new JpaChatService(chatRepository);
    }

    @Bean
    public LinkService jpaLinkService(
        JpaChatRepository chatRepository,
        JpaLinkRepository linkRepository,
        List<LinkInfoReceiver> clients
    ) {
        return new JpaLinkService(linkRepository, chatRepository, clients);
    }
}
