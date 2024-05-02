package edu.java.configuration;

import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.repository.chat.JooqChatRepository;
import edu.java.repository.chat_link.JooqChatLinkRepository;
import edu.java.repository.link.JooqLinkRepository;
import edu.java.service.chat.ChatService;
import edu.java.service.chat.DefaultChatService;
import edu.java.service.link.DefaultLinkService;
import edu.java.service.link.LinkService;
import java.util.List;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqConfiguration {

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    @Bean
    public ChatService jooqChatService(JooqChatRepository chatRepository) {
        return new DefaultChatService(chatRepository);
    }

    @Bean
    public LinkService jooqLinkService(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository,
        JooqChatRepository chatRepository,
        List<LinkInfoReceiver> clients
    ) {
        return new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, clients);
    }
}
