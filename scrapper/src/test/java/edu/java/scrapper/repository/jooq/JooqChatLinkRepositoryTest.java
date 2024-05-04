package edu.java.scrapper.repository.jooq;

import edu.java.link_type_resolver.LinkType;
import edu.java.repository.chat_link.JooqChatLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
public class JooqChatLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private JooqChatLinkRepository jooqChatLinkRepository;

    @Transactional
    @Rollback
    @Test
    public void add_shouldCorrectlyAddDataInChatLinkTable_whenIdExistsInTables() {
        //Arrange
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

        //Act
        jooqChatLinkRepository.add(chatId, linkId);
        //Assert
        Boolean hasBeenAdded =
            jdbcClient.sql("SELECT COUNT(*) FROM chat_link WHERE link_id = :linkId AND chat_id = :chatId")
                .param("linkId", linkId)
                .param("chatId", chatId)
                .query(Boolean.class)
                .single();
        assertThat(hasBeenAdded).isTrue();
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
        jooqChatLinkRepository.add(linkId, chatId);

        // Assert
        Long count = jdbcClient.sql("SELECT COUNT(*) FROM chat_link WHERE link_id = :linkId AND chat_id = :chatId")
            .param("linkId", linkId)
            .param("chatId", chatId)
            .query(Long.class)
            .single();
        assertThat(count).isEqualTo(1L);
    }

    @Transactional
    @Rollback
    @Test
    public void remove_shouldCorrectlyRemoveDataFromChatLinkTable_whenIdExistsInChatLinkTable() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        String url = "google.com";
        jdbcClient.sql("""
                INSERT INTO link(id, url, type, updated_at, last_checked_at)
                VALUES (:id, :url, :link_type, :updated_at, :last_checked_at)""")
            .param("id", linkId)
            .param("url", url);
    }
}
