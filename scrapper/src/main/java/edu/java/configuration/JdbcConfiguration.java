package edu.java.configuration;

import edu.java.repository.chat.ChatRepository;
import edu.java.repository.chat.JdbcChatRepository;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.chat_link.JdbcChatLinkRepository;
import edu.java.repository.link.JdbcLinkRepository;
import edu.java.repository.link.LinkRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfiguration {
    @Bean
    public ChatRepository jdbcChatRepository(JdbcClient jdbcClient) {
        return new JdbcChatRepository(jdbcClient);
    }

    @Bean
    public LinkRepository jdbcLinkRepository(JdbcClient jdbcClient) {
        return new JdbcLinkRepository(jdbcClient);
    }

    @Bean
    public ChatLinkRepository jdbcChatLinkRepository(JdbcClient jdbcClient) {
        return new JdbcChatLinkRepository(jdbcClient);
    }
}

