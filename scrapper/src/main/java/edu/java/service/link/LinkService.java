package edu.java.service.link;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.ChatLinkResponse;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface LinkService {

    ListLinksResponse getAllLinks(Long chatId);

    ResponseLink addLink(Long chatId, AddLinkRequest addLinkRequest);

    ResponseLink removeLink(long chatId, RemoveLinkRequest removeLinkRequest);

    List<ChatLinkResponse> findAllChatsByLinksFiltered(OffsetDateTime time);

    Optional<LinkData> findById(long linkId);

    void updateInfo(long linkId, OffsetDateTime updatedAt);

}
