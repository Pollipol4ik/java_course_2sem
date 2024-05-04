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
import edu.java.link_type_resolver.LinkType;
import edu.java.repository.jpa.chat.JpaChatRepository;
import edu.java.repository.jpa.entity.ChatEntity;
import edu.java.repository.jpa.entity.LinkEntity;
import edu.java.repository.jpa.link.JpaLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;
    private final List<LinkInfoReceiver> clients;

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse getAllLinks(Long chatId) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotAuthorizedException::new);
        List<ResponseLink> linkResponses = chatEntity.getLinks().stream()
            .map(linkEntity -> new ResponseLink(linkEntity.getLinkId(), linkEntity.getUrl()))
            .collect(Collectors.toList());
        return new ListLinksResponse(linkResponses);
    }

    @Override
    @Transactional
    public ResponseLink addLink(Long chatId, AddLinkRequest addLinkRequest) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotAuthorizedException::new);
        for (LinkInfoReceiver client : clients) {
            if (client.isValidate(URI.create(addLinkRequest.link()))) {
                Optional<LinkEntity> optionalLinkEntity = linkRepository.findByUrl(addLinkRequest.link());
                LinkEntity linkEntity;
                if (optionalLinkEntity.isEmpty()) {
                    client.receiveLastUpdateTime(URI.create(addLinkRequest.link()));
                    linkEntity =
                        linkRepository.save(new LinkEntity(addLinkRequest.link(), client.getLinkType().name()));
                } else {
                    linkEntity = optionalLinkEntity.get();
                    if (chatEntity.getLinks().contains(linkEntity)) {
                        throw new LinkAlreadyTrackedException(addLinkRequest);
                    }
                }
                chatEntity.addLink(linkEntity);
                return new ResponseLink(linkEntity.getLinkId(), linkEntity.getUrl());
            }
        }
        throw new UnsupportedLinkTypeException(addLinkRequest.link());
    }

    @Override
    @Transactional
    public ResponseLink removeLink(long chatId, RemoveLinkRequest removeLinkRequest) {
        ChatEntity chatEntity = chatRepository.findById(chatId).orElseThrow(ChatNotAuthorizedException::new);
        Optional<LinkEntity> linkEntity = linkRepository.findById(removeLinkRequest.linkId());
        if (linkEntity.isPresent() && chatEntity.getLinks().contains(linkEntity.get())) {
            LinkEntity link = linkEntity.get();
            ResponseLink response = new ResponseLink(link.getLinkId(), link.getUrl());
            chatEntity.getLinks().remove(link);
            link.getChats().remove(chatEntity);
            if (link.getChats().isEmpty()) {
                linkRepository.delete(link);
            }
            return response;
        }
        throw new LinkNotFoundException(removeLinkRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatLinkResponse> findAllChatsByLinksFiltered(OffsetDateTime time) {
        Set<LinkEntity> links = linkRepository.findAllByCheckedAtBefore(time);
        return links.stream()
            .map(linkEntity -> new ChatLinkResponse(
                    linkEntity.getLinkId(),
                    linkEntity.getChats().stream().map(ChatEntity::getChatId).collect(Collectors.toSet())
                )
            )
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LinkData> findById(long linkId) {
        Optional<LinkEntity> linkEntityOpt = linkRepository.findById(linkId);
        return linkEntityOpt.map(linkEntity -> new LinkData(
            linkId,
            linkEntity.getUrl(),
            LinkType.valueOf(linkEntity.getType()),
            linkEntity.getLastUpdateTime(),
            linkEntity.getCheckedAt()
        ));
    }

    @Override
    @Transactional
    public void updateInfo(long linkId, OffsetDateTime updatedAt) {
        Optional<LinkEntity> linkEntityOpt = linkRepository.findById(linkId);
        linkEntityOpt.ifPresent(linkEntity -> {
            linkEntity.setLastUpdateTime(updatedAt);
            linkRepository.save(linkEntity);
        });
    }
}
