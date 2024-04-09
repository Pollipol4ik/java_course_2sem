package edu.java.scrapper.repository;

import edu.java.dto.Chat;
import edu.java.link_type_resolver.LinkType;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
public class JdbcChatLinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private ChatLinkRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jdbc");
    }

    @Test
    @Transactional
    @Rollback
    public void add_shouldAddChatLink_whenNotExists() {
        // Arrange
        long chatId = 1L;
        long linkId = 1L;
        String url = "google.com";

        jdbcClient.sql("""
                INSERT INTO link(id, url, type, updated_at, last_checked_at)
                VALUES (:id, :url, :link_type, :updated_at, :last_checked_at)""")
            .param("id", linkId)
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .param("updated_at", null)
            .param("last_checked_at", OffsetDateTime.now())
            .update();

        jdbcClient.sql("INSERT INTO chat(id) VALUES (:id)")
            .param("id", chatId)
            .update();

        // Act
        repository.add(linkId, chatId);

        // Assert
        Long count = jdbcClient.sql("SELECT COUNT(*) FROM chat_link WHERE link_id = :linkId AND chat_id = :chatId")
            .param("linkId", linkId)
            .param("chatId", chatId)
            .query(Long.class)
            .single();
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void remove_shouldRemoveChatLink_whenExists() {
        // Arrange
        long chatId = 1L;
        long linkId = 1L;
        String url = "google.com";
        jdbcClient.sql("""
                INSERT INTO link(id, url, type, updated_at, last_checked_at)
                VALUES (:id, :url, :link_type, :updated_at, :last_checked_at)""")
            .param("id", linkId)
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .param("updated_at", null)
            .param("last_checked_at", OffsetDateTime.now())
            .update();

        jdbcClient.sql("INSERT INTO chat(id) VALUES (:id)")
            .param("id", chatId)
            .update();

        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", chatId)
            .update();

        // Act
        repository.remove(linkId, chatId);

        // Assert
        Long count = jdbcClient.sql("SELECT COUNT(*) FROM chat_link WHERE link_id = :linkId AND chat_id = :chatId")
            .param("linkId", linkId)
            .param("chatId", chatId)
            .query(Long.class)
            .single();
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void removeByLinkId_shouldRemoveAllChatLinksRelatedToLinkId() {
        // Arrange
        long chatId1 = 1L;
        long chatId2 = 2L;
        long linkId = 1L;
        String url = "google.com";
        jdbcClient.sql("""
                INSERT INTO link(id, url, type, updated_at, last_checked_at)
                VALUES (:id, :url, :link_type, :updated_at, :last_checked_at)""")
            .param("id", linkId)
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .param("updated_at", null)
            .param("last_checked_at", OffsetDateTime.now())
            .update();

        jdbcClient.sql("INSERT INTO chat(id) VALUES (:id)")
            .param("id", chatId1)
            .update();

        jdbcClient.sql("INSERT INTO chat(id) VALUES (:id)")
            .param("id", chatId2)
            .update();
        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", chatId1)
            .update();
        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", chatId2)
            .update();

        // Act
        repository.removeByLinkId(linkId);

        // Assert
        Long count = jdbcClient.sql("SELECT COUNT(*) FROM chat_link WHERE link_id = :linkId")
            .param("linkId", linkId)
            .query(Long.class)
            .single();
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllByLinkId_shouldReturnListOfChatsRelatedToLinkId() {
        // Arrange
        long chatId1 = 1L;
        long chatId2 = 2L;
        long linkId = 1L;
        String url = "google.com";
        jdbcClient.sql("""
                INSERT INTO link(id, url, type, updated_at, last_checked_at)
                VALUES (:id, :url, :link_type, :updated_at, :last_checked_at)""")
            .param("id", linkId)
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .param("updated_at", null)
            .param("last_checked_at", OffsetDateTime.now())
            .update();

        jdbcClient.sql("INSERT INTO chat(id) VALUES (:id)")
            .param("id", chatId1)
            .update();

        jdbcClient.sql("INSERT INTO chat(id) VALUES (:id)")
            .param("id", chatId2)
            .update();
        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", chatId1)
            .update();
        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", chatId2)
            .update();

        // Act
        List<Chat> chats = repository.findAllByLinkId(linkId);

        // Assert
        assertThat(chats.size()).isEqualTo(2);
    }
}
