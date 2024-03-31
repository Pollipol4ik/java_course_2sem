//package edu.java.scrapper.repository.jooq;
//
//import edu.java.dto.Chat;
//import edu.java.dto.ChatLinkResponse;
//import edu.java.repository.chat_link.ChatLinkRepository;
//import java.time.OffsetDateTime;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class JooqChatLinkRepositoryTest {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    @Autowired
//    private ChatLinkRepository repository;
//
//    @Transactional
//    @Rollback
//    @Test
//    public void removeByLinkId_shouldRemoveAllLinksByLinkId() {
//        // Arrange
//        long chatId = 1L;
//        long linkId = 1L;
//        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId);
//        jdbcTemplate.update("INSERT INTO link (id, url, type, updated_at, last_checked_at) VALUES (?, ?, ?, ?, ?)",
//            linkId, "http://example.com", "example", OffsetDateTime.now(), OffsetDateTime.now());
//        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
//
//        // Act
//        repository.removeByLinkId(linkId);
//
//        // Assert
//        List<Chat> chats = jdbcTemplate.query("SELECT * FROM chat_link WHERE link_id = ?", new Object[] {linkId},
//            (resultSet, rowNum) -> new Chat(resultSet.getLong("chat_id"))
//        );
//        assertThat(chats).isEmpty();
//    }
//
//    @Transactional
//    @Rollback
//    @Test
//    public void findAllByLinkId_shouldReturnChatsByLinkId() {
//        // Arrange
//        long chatId = 1L;
//        long linkId = 1L;
//        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId);
//        jdbcTemplate.update("INSERT INTO link (id, url, type, updated_at, last_checked_at) VALUES (?, ?, ?, ?, ?)",
//            linkId, "http://example.com", "example", OffsetDateTime.now(), OffsetDateTime.now());
//        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
//
//        // Act
//        List<Chat> chats = repository.findAllByLinkId(linkId);
//
//        // Assert
//        assertThat(chats).hasSize(1);
//        assertThat(chats.get(0).chatId()).isEqualTo(chatId);
//    }
//
//    @Transactional
//    @Rollback
//    @Test
//    public void findAllFiltered_shouldReturnFilteredChatLinkResponses() {
//        // Arrange
//        long chatId1 = 1L;
//        long chatId2 = 2L;
//        long linkId1 = 1L;
//        long linkId2 = 2L;
//        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId1);
//        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId2);
//        jdbcTemplate.update("INSERT INTO link (id, url, type, updated_at, last_checked_at) VALUES (?, ?, ?, ?, ?)",
//            linkId1, "http://example.com/1", "example", OffsetDateTime.now(), OffsetDateTime.now());
//        jdbcTemplate.update("INSERT INTO link (id, url, type, updated_at, last_checked_at) VALUES (?, ?, ?, ?, ?)",
//            linkId2, "http://example.com/2", "example", OffsetDateTime.now(), OffsetDateTime.now());
//        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId1, linkId1);
//        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId2, linkId2);
//
//        // Act
//        OffsetDateTime time = OffsetDateTime.now().minusDays(1); // Assuming some time in the past
//        List<ChatLinkResponse> responses = repository.findAllFiltered(time);
//
//        // Assert
//        assertThat(responses).isNotEmpty();
//        assertThat(responses).extracting("linkId").containsExactlyInAnyOrder(linkId1, linkId2);
//    }
//}
