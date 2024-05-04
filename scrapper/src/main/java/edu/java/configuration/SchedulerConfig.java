package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.link_information.LinkInformationReceiverProvider;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.link.LinkRepository;
import edu.java.scheduler.LinkUpdaterScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkInformationReceiverProvider linkInformationReceiverManager;
    private final BotClient botClient;

    @Bean
    public LinkUpdaterScheduler linkUpdaterScheduler() {
        return new LinkUpdaterScheduler(linkRepository, chatLinkRepository, linkInformationReceiverManager, botClient);
    }
}
