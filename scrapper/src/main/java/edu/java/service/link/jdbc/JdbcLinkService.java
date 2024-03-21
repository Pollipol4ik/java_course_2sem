package edu.java.service.link.jdbc;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UnsupportedLinkTypeException;
import edu.java.link_type_resolver.LinkType;
import edu.java.link_type_resolver.LinkTypeResolver;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.link.LinkRepository;
import edu.java.service.link.LinkService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkTypeResolver linkTypeResolver;

    @Override
    @Transactional
    public ListLinksResponse getAllLinks(Long chatId) {
        return linkRepository.findAllByChatId(chatId);
    }

    @Override
    @Transactional
    public ResponseLink addLink(Long chatId, AddLinkRequest addLinkRequest) {
        LinkType linkType = linkTypeResolver.resolve(addLinkRequest.link());
        if (linkType.equals(LinkType.UNKNOWN)) {
            throw new UnsupportedLinkTypeException(addLinkRequest);
        }
        long linkId = linkRepository.add(chatId, addLinkRequest, linkType);
        chatLinkRepository.add(linkId, chatId);
        return new ResponseLink(linkId, addLinkRequest.link());
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
}
