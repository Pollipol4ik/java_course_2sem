package edu.java.repository.chat_link;

import edu.java.dto.Chat;
import edu.java.dto.ChatLinkResponse;
import edu.java.repository.jooq.tables.ChatLink;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import static edu.java.repository.jooq.tables.ChatLink.CHAT_LINK;
import static edu.java.repository.jooq.tables.Link.LINK;

@Log4j2
@RequiredArgsConstructor
public class JooqChatLinkRepository implements ChatLinkRepository {
    private final DSLContext context;
    @Value("${spring.database.limit}")
    private int limit;

    @Override
    public void add(long linkId, long chatId) {
        context.insertInto(ChatLink.CHAT_LINK, ChatLink.CHAT_LINK.CHAT_ID, ChatLink.CHAT_LINK.LINK_ID)
            .values(chatId, linkId)
            .onDuplicateKeyIgnore()
            .execute();
    }

    @Override
    public void remove(long linkId, long chatId) {
        context.deleteFrom(ChatLink.CHAT_LINK)
            .where(ChatLink.CHAT_LINK.LINK_ID.eq(linkId).and(ChatLink.CHAT_LINK.CHAT_ID.eq(chatId)))
            .execute();
    }

    @Override
    public void removeByLinkId(long linkId) {
        context.deleteFrom(ChatLink.CHAT_LINK)
            .where(ChatLink.CHAT_LINK.LINK_ID.eq(linkId))
            .execute();
    }

    @Override
    public List<Chat> findAllByLinkId(long linkId) {
        return context.selectFrom(ChatLink.CHAT_LINK)
            .where(ChatLink.CHAT_LINK.LINK_ID.eq(linkId))
            .fetchInto(Chat.class);
    }

    @Override
    public List<ChatLinkResponse> findAllFiltered(OffsetDateTime time) {
        return context
            .select(LINK.ID, CHAT_LINK.CHAT_ID)
            .from(LINK)
            .join(CHAT_LINK).on(LINK.ID.eq(CHAT_LINK.LINK_ID))
            .where(LINK.LAST_CHECKED_AT.lt(time))
            .limit(limit)
            .fetch()
            .intoGroups(LINK.ID, rec -> rec.get(CHAT_LINK.CHAT_ID))
            .entrySet()
            .stream()
            .map(entry -> new ChatLinkResponse(entry.getKey(), new HashSet<>(entry.getValue())))
            .toList();
    }

    @Override
    public boolean isTracked(Long chatId, Long linkId) {
        long count = context.fetchCount(CHAT_LINK, CHAT_LINK.LINK_ID.eq(linkId).and(CHAT_LINK.CHAT_ID.eq(chatId)));
        return count > 0;
    }
}
