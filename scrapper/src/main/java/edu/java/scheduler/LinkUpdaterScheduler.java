package edu.java.scheduler;

import edu.java.client.BotClient;
import edu.java.client.link_information.LastUpdateTime;
import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.client.link_information.LinkInformationReceiverProvider;
import edu.java.dto.Chat;
import edu.java.dto.LinkData;
import edu.java.dto.UpdateLink;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.link.LinkRepository;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private static final Duration CHECK_PERIOD = Duration.ofSeconds(15);
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkInformationReceiverProvider linkInformationReceiverManager;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {

        log.info("Update has been started");
        List<LinkData> links = linkRepository.findUncheckedLinks(CHECK_PERIOD);

        log.info("Updating {}", links);
        List<UpdateLink> linkUpdates = new ArrayList<>();
        for (LinkData link : links) {
            LinkInfoReceiver linkInformationProvider =
                linkInformationReceiverManager.getReceiver(link.type());
            LastUpdateTime lastUpdateTime = linkInformationProvider.receiveLastUpdateTime(URI.create(link.url()));
            if (!lastUpdateTime.lastUpdateTime().equals(link.updatedAt()) && link.updatedAt() != null) {
                linkUpdates.add(new UpdateLink(
                    link.id(),
                    URI.create(link.url()),
                    "Произошли изменения",
                    getChatIds(link.id())
                ));
            }
            linkRepository.updateInfo(link.id(), lastUpdateTime.lastUpdateTime());
        }

        log.info("Sending updates: {}", linkUpdates);
        for (UpdateLink linkUpdate : linkUpdates) {
            botClient.sendUpdate(linkUpdate);
        }
    }

    private List<Long> getChatIds(long id) {
        List<Chat> chats = chatLinkRepository.findAllByLinkId(id);
        return chats.stream().map(Chat::chatId).toList();
    }
}
