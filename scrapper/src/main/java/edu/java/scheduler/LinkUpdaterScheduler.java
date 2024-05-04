package edu.java.scheduler;

import edu.java.client.BotClient;
import edu.java.client.link_information.LinkInfo;
import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.dto.ChatLinkResponse;
import edu.java.dto.LinkData;
import edu.java.dto.UpdateLink;
import edu.java.service.link.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Component
public class LinkUpdaterScheduler {
    private final List<LinkInfoReceiver> linkInfoReceivers;
    private final BotClient botClient;
    private final LinkService linkService;

    @Value("${spring.database.check-time-minutes}")
    private int minutesCheckTime;

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    @Transactional
    public void update() {
        OffsetDateTime time = OffsetDateTime.now();
        time = time.minusMinutes(minutesCheckTime);
        List<ChatLinkResponse> linksToChats = linkService.findAllChatsByLinksFiltered(time);
        if (linksToChats != null) {
            log.info(linksToChats);
            for (ChatLinkResponse linkToChats : linksToChats) {
                Long linkId = linkToChats.linkId();
                LinkData data = linkService.findById(linkId).get();
                for (LinkInfoReceiver client : linkInfoReceivers) {
                    if (client.isValidate(URI.create(data.url()))) {
                        List<LinkInfo> listLinkInfo = client.receiveLastUpdateTime(URI.create(data.url()))
                            .stream()
                            .filter(linkInfo -> linkInfo.lastActivityDate().isAfter(data.updatedAt()))
                            .sorted(Comparator.comparing(LinkInfo::lastActivityDate))
                            .toList();
                        log.info(listLinkInfo);
                        for (LinkInfo info : listLinkInfo) {
                            UpdateLink update = new UpdateLink(
                                linkId,
                                URI.create(data.url()),
                                info.title(),
                                linkToChats.tgChatIds().stream().toList()
                            );
                            botClient.sendUpdate(update);
                        }
                        if (!listLinkInfo.isEmpty()) {
                            OffsetDateTime curTime = OffsetDateTime.now();
                            linkService.updateInfo(
                                linkId,
                                curTime
                            );
                        }
                    }
                }
            }
        }

    }

}
