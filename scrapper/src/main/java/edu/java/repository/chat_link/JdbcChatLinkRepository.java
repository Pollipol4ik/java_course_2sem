package edu.java.repository.chat_link;

import edu.java.dto.Chat;
import edu.java.dto.ChatLinkResponse;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class JdbcChatLinkRepository implements ChatLinkRepository {

    private final JdbcClient jdbcClient;

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Override
    public void add(long linkId, long chatId) {
        jdbcClient.sql("""
                INSERT INTO chat_link(chat_id, link_id)
                VALUES(:chat_id, :link_id)
                ON CONFLICT (chat_id, link_id) DO NOTHING""")
            .param("chat_id", chatId)
            .param("link_id", linkId)
            .update();
    }

    @Override
    public void remove(long linkId, long chatId) {
        jdbcClient.sql("""
                DELETE FROM chat_link
                WHERE link_id = :link_id AND chat_id = :chat_id""")
            .param("link_id", linkId)
            .param("chat_id", chatId)
            .update();
    }

    @Override
    public void removeByLinkId(long linkId) {
        jdbcClient.sql("""
                DELETE FROM chat_link
                WHERE link_id = :link_id""")
            .param("link_id", linkId)
            .update();
    }

    @Override
    public List<Chat> findAllByLinkId(long linkId) {
        return jdbcClient.sql("""
                SELECT * FROM chat_link
                WHERE link_id = :link_id""")
            .param("link_id", linkId)
            .query(Chat.class)
            .list();
    }

    @Override
    public List<ChatLinkResponse> findAllFiltered(OffsetDateTime time) {
        return jdbcClient.sql(
                "SELECT link.id, link.last_checked_at, chat_link.chat_id "
                    + "FROM link JOIN chat_link ON chat_link.link_id = link.id "
                    + "WHERE link.last_checked_at < :time "
                    + "LIMIT 10")
            .param("time", time).query(ChatLinkResponse.class)
            .list();
    }

    @Override
    public boolean isTracked(Long chatId, Long linkId) {
        return false;
    }

}
