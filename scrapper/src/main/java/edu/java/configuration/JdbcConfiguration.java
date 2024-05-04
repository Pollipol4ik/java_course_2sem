package edu.java.configuration;

import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.repository.chat.JdbcChatRepository;
import edu.java.repository.chat_link.JdbcChatLinkRepository;
import edu.java.repository.link.JdbcLinkRepository;
import edu.java.service.chat.ChatService;
import edu.java.service.chat.DefaultChatService;
import edu.java.service.link.DefaultLinkService;
import edu.java.service.link.LinkService;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfiguration {

    @Bean
    public ChatService jdbcChatService(JdbcChatRepository jdbcChatRepository) {
        return new DefaultChatService(jdbcChatRepository);
    }

    @Bean
    public LinkService jdbcLinkService(
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository,
        JdbcChatRepository chatRepository,
        List<LinkInfoReceiver> clients
    ) {
        return new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, clients);
    }
}
