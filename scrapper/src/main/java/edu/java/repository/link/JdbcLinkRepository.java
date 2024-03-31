package edu.java.repository.link;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.ResponseLink;
import edu.java.link_type_resolver.LinkType;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private final JdbcClient jdbcClient;

    @Override
    public ListLinksResponse findAll(long chatId) {
        return new ListLinksResponse(jdbcClient.sql("""
                SELECT id, url AS link FROM link""")
            .query(ResponseLink.class)
            .list());
    }

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Override
    public long add(long chatId, AddLinkRequest addLinkRequest, LinkType linkType) {
        return jdbcClient.sql("""
                INSERT INTO link(url, type, updated_at, last_checked_at)
                VALUES (:url, :link_type, :updated_at, :last_checked_at)
                ON CONFLICT (url) DO UPDATE SET updated_at = :updated_at, last_checked_at = :last_checked_at
                RETURNING id""")
            .param("url", addLinkRequest.link())
            .param("link_type", linkType.name())
            .param("updated_at", null)
            .param("last_checked_at", OffsetDateTime.now())
            .query(Long.class)
            .single();
    }

    @Override
    public ListLinksResponse findAllByChatId(long chatId) {
        return new ListLinksResponse(jdbcClient.sql("""
                SELECT link_id, link.url AS link FROM link
                JOIN chat_link ON link.id = chat_link.link_id
                WHERE chat_id = :chatId""")
            .param("chatId", chatId)
            .query(ResponseLink.class)
            .list());
    }

    @SuppressWarnings("checkstyle:MultipleStringLiterals")
    @Override
    public void remove(long linkId) {
        jdbcClient.sql("""
                DELETE FROM link
                WHERE id = :link_id""")
            .param("link_id", linkId)
            .update();
    }

    public Optional<LinkData> findByUrl(String url) {
        try {
            return Optional.of(jdbcClient.sql(
                    "SELECT id, url, type, updated_at, last_checked_at FROM link WHERE url = :url")
                .param("url", url)
                .query(LinkData.class)
                .single());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<LinkData> findById(long linkId) {
        return Optional.of(jdbcClient.sql(
                "SELECT id, url, type, updated_at, last_checked_at FROM link WHERE id = :id")
            .param("id", linkId)
            .query(LinkData.class)
            .single());
    }

    @Override
    public List<LinkData> findUncheckedLinks(Duration checkPeriod) {
        return jdbcClient.sql("""
                SELECT id, url, type, updated_at, last_checked_at FROM link
                WHERE last_checked_at < :last_checked_at""")
            .param("last_checked_at", OffsetDateTime.now().minus(checkPeriod))
            .query(LinkData.class)
            .list();
    }

    @Override
    public void updateInfo(long linkId, OffsetDateTime updatedAt) {
        jdbcClient.sql("""
                UPDATE link SET last_checked_at = :last_checked_at, updated_at = :updated_at
                WHERE id = :link_id""")
            .param("last_checked_at", OffsetDateTime.now())
            .param("updated_at", updatedAt)
            .param("link_id", linkId)
            .update();
    }
}
