package edu.java.service.link;

import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.ChatLinkResponse;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.exception.ChatNotAuthorizedException;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UnsupportedLinkTypeException;
import edu.java.repository.chat.ChatRepository;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.link.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DefaultLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final ChatRepository chatRepository;
    private final List<LinkInfoReceiver> linkInfoReceivers;

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse getAllLinks(Long chatId) {
        return linkRepository.findAll(chatId);
    }

    @Override
    @Transactional
    public ResponseLink addLink(Long chatId, AddLinkRequest addLinkRequest) {
        if (!chatRepository.doesExist(chatId)) {
            throw new ChatNotAuthorizedException();
        }
        for (LinkInfoReceiver client : linkInfoReceivers) {
            if (client.isValidate(URI.create(addLinkRequest.link()))) {
                var linkData = linkRepository.findByUrl(addLinkRequest.link());
                if (linkData.isEmpty()) {
                    client.receiveLastUpdateTime(URI.create(addLinkRequest.link()));
                    long linkId = linkRepository.add(chatId, addLinkRequest, linkData.get().type());
                    chatLinkRepository.add(chatId, linkId);
                }
                if (chatLinkRepository.isTracked(chatId, linkData.get().id())) {
                    throw new LinkAlreadyTrackedException(addLinkRequest);
                }
                chatLinkRepository.add(linkData.get().id(), chatId);
                return new ResponseLink(linkData.get().id(), linkData.get().url());
            }
        }
        throw new UnsupportedLinkTypeException(addLinkRequest.link());
    }

    @Override
    @Transactional
    public ResponseLink removeLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        Optional<LinkData> optionalLink = linkRepository.findById(removeLinkRequest.linkId());
        if (optionalLink.isEmpty()) {
            throw new LinkNotFoundException(removeLinkRequest);
        }
        LinkData link = optionalLink.get();
        chatLinkRepository.remove(link.id(), chatId);
        if (chatLinkRepository.findAllByLinkId(link.id()).isEmpty()) {
            linkRepository.remove(link.id());
        }
        return new ResponseLink(link.id(), link.url());

    }

    @Override
    @Transactional
    public List<ChatLinkResponse> findAllChatsByLinksFiltered(OffsetDateTime time) {
        return chatLinkRepository.findAllFiltered(time);
    }

    @Override
    @Transactional
    public Optional<LinkData> findById(long linkId) {
        return linkRepository.findById(linkId);
    }

    @Override
    @Transactional
    public void updateInfo(long linkId, OffsetDateTime updatedAt) {
        linkRepository.updateInfo(linkId, updatedAt);
    }

}
