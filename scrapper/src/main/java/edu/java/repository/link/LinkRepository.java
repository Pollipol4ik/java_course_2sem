package edu.java.repository.link;

import edu.java.client.link_information.LastUpdateTime;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;

public interface LinkRepository {
    ListLinksResponse findAll(Long chatId);

    ResponseLink add(Long chatId, AddLinkRequest addLinkRequest);

    ResponseLink remove(Long chatId, RemoveLinkRequest removeLinkRequest);

    LinkData getData(Long linkId);

    void updateLink(LastUpdateTime info);

    Long getLinkId(String url);
}
