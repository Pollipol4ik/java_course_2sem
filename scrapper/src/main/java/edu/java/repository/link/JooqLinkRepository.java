package edu.java.repository.link;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.ResponseLink;
import edu.java.link_type_resolver.LinkType;
import edu.java.repository.jooq.tables.ChatLink;
import edu.java.repository.jooq.tables.Link;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;

@RequiredArgsConstructor
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext context;

    @Override
    public ListLinksResponse findAll(long chatId) {
        List<ResponseLink> responseLinks = context.select(Link.LINK.ID, Link.LINK.URL.as("link"))
            .from(Link.LINK)
            .fetchInto(ResponseLink.class);

        return new ListLinksResponse(responseLinks);
    }

    @Override
    public long add(long chatId, AddLinkRequest addLinkRequest, LinkType linkType) {
        OffsetDateTime now = OffsetDateTime.now();
        Record linkRecord = context.insertInto(Link.LINK)
            .set(Link.LINK.URL, addLinkRequest.link())
            .set(Link.LINK.TYPE, linkType.name())
            .set(Link.LINK.UPDATED_AT, now)
            .set(Link.LINK.LAST_CHECKED_AT, now)
            .onDuplicateKeyUpdate()
            .set(Link.LINK.UPDATED_AT, now)
            .set(Link.LINK.LAST_CHECKED_AT, now)
            .returning(Link.LINK.ID)
            .fetchOne();
        return linkRecord.get(Link.LINK.ID);
    }

    @Override
    public void remove(long linkId) {
        context.deleteFrom(Link.LINK)
            .where(Link.LINK.ID.eq(linkId))
            .execute();
    }

    @Override
    public Optional<LinkData> findByUrl(String url) {
        LinkData data = context.selectFrom(Link.LINK)
            .where(Link.LINK.URL.eq(url))
            .fetchOneInto(LinkData.class);
        return Optional.ofNullable(data);
    }

    @Override
    public Optional<LinkData> findById(long linkId) {
        LinkData data = context.selectFrom(Link.LINK)
            .where(Link.LINK.ID.eq(linkId))
            .fetchOneInto(LinkData.class);
        return Optional.ofNullable(data);
    }

    @Override
    public ListLinksResponse findAllByChatId(long chatId) {
        List<ResponseLink> responseLinks = context.select(Link.LINK.ID.as("link_id"), Link.LINK.URL.as("link"))
            .from(Link.LINK)
            .join(ChatLink.CHAT_LINK).on(Link.LINK.ID.eq(ChatLink.CHAT_LINK.LINK_ID))
            .where(ChatLink.CHAT_LINK.CHAT_ID.eq(chatId))
            .fetchInto(ResponseLink.class);

        return new ListLinksResponse(responseLinks);
    }

    @Override
    public List<LinkData> findUncheckedLinks(Duration checkPeriod) {
        OffsetDateTime limitDateTime = OffsetDateTime.now().minus(checkPeriod);
        List<LinkData> links = context.selectFrom(Link.LINK)
            .where(Link.LINK.LAST_CHECKED_AT.lt(limitDateTime))
            .fetchInto(LinkData.class);
        return links;
    }

    @Override
    public void updateInfo(long linkId, OffsetDateTime updatedAt) {
        context.update(Link.LINK)
            .set(Link.LINK.LAST_CHECKED_AT, OffsetDateTime.now())
            .set(Link.LINK.UPDATED_AT, updatedAt)
            .where(Link.LINK.ID.eq(linkId))
            .execute();
    }
}
