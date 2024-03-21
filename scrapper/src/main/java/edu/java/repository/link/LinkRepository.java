package edu.java.repository.link;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.link_type_resolver.LinkType;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {

    ListLinksResponse findAll(long chatId);

    long add(long chatId, AddLinkRequest addLinkRequest, LinkType linkType);

    void remove(long linkId);

    Optional<LinkData> findByUrl(String url);

    Optional<LinkData> findById(long linkId);

    ListLinksResponse findAllByChatId(long chatId);

    List<LinkData> findUncheckedLinks(Duration checkPeriod);

    void updateInfo(long linkId, OffsetDateTime updatedAt);
}
