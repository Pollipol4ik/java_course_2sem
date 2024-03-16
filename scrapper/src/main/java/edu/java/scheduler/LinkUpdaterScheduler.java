package edu.java.scheduler;

import edu.java.client.BotClient;
import edu.java.client.link_information.LastUpdateTime;
import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.dto.ChatLinkResponse;
import edu.java.dto.LinkData;
import edu.java.dto.UpdateLink;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.link.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    @Value("${spring.database.check-time-minutes}")
    private int minutesCheckTime;
    private final List<LinkInfoReceiver> clientInfoProviders;
    private final BotClient botClient;
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    @Transactional
    public void update() {
        OffsetDateTime time = OffsetDateTime.now();
        time = time.minusMinutes(minutesCheckTime);
        List<ChatLinkResponse> linksToChats = chatLinkRepository.findAllFiltered(time);
        log.info(linksToChats);
        for (ChatLinkResponse linkToChats : linksToChats) {
            Long linkId = linkToChats.linkId();
            LinkData data = linkRepository.getData(linkId);
            UpdateLink update = new UpdateLink(
                linkId,
                data.url(),
                "Ссылка была обновлена: ",
                linkToChats.tgChatIds().stream().toList()
            );
            for (LinkInfoReceiver client : clientInfoProviders) {
                if (client.isValidate(data.url())) {
                    LastUpdateTime info = client.receiveLastUpdateTime(data.url());
                    if (info.lastUpdateTime().isAfter(data.updateTime())) {
                        log.info("here");
                        linkRepository.updateLink(info);
                        botClient.sendUpdate(update);
                    }
                }
            }
        }
    }
}

