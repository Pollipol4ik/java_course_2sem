package edu.java.service.link.jdbc;

import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.exception.ChatNotAuthorizedException;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.repository.chat.ChatRepository;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.link.LinkRepository;
import edu.java.service.link.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final ChatRepository chatRepository;
    private final List<LinkInfoReceiver> clients;

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse getAllLinks(Long chatId) {
        return linkRepository.findAll(chatId);
    }


    @Override
    public ResponseLink removeLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        ResponseLink response = chatLinkRepository.remove(chatId, removeLinkRequest.linkId());
        if (!chatLinkRepository.hasChats(removeLinkRequest.linkId())) {
            return linkRepository.remove(chatId, removeLinkRequest);
        }
        return response;
    }

    @Override
    @Transactional
    public ResponseLink addLink(Long chatId, AddLinkRequest addLinkRequest) {
        if (!chatRepository.isInTable(chatId)) {
            throw new ChatNotAuthorizedException();
        }
        for (LinkInfoReceiver client : clients) {
            if (client.isValidate(URI.create(addLinkRequest.link()))) {
                Long linkId = linkRepository.getLinkId(addLinkRequest.link().toString());
                if (linkId == 0) {
                    client.receiveLastUpdateTime(URI.create(addLinkRequest.link()));
                    ResponseLink response = linkRepository.add(chatId, addLinkRequest);
                    chatLinkRepository.add(chatId, response.linkId());
                    return response;
                }
                if (chatLinkRepository.isTracked(chatId, linkId)) {
                    throw new LinkAlreadyTrackedException(addLinkRequest);
                }
                chatLinkRepository.add(chatId, linkId);
                return new ResponseLink(linkId, linkRepository.getData(linkId).url().toString());
            }
        }
        throw new UnsupportedOperationException(addLinkRequest.link());
    }
}
