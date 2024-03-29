//package edu.java.scrapper.repository;
//
//import edu.java.dto.Chat;
//import edu.java.repository.chat_link.ChatLinkRepository;
//import java.util.List;
//import edu.java.scrapper.IntegrationEnvironment;
//import org.junit.jupiter.api.Test;
//import org.springframework.jdbc.core.simple.JdbcClient;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//public class JdbcChatLinkRepositoryTest extends IntegrationEnvironment {
//
//
//    private JdbcClient jdbcClient;
//
//
//    private ChatLinkRepository repository;
//
//    @Transactional
//    @Rollback
//    @Test
//    public void findAllByLinkId_shouldReturnCorrectChats_whenLinkExists() {
//
//        Long existingLinkId = jdbcClient.sql("""
//                INSERT INTO link(url, type, updated_at, last_checked_at)
//                VALUES ('existing.com', 'example_type', now(), now())
//                RETURNING id
//                """)
//            .query(Long.class)
//            .single();
//
//        long chatId1 = 1L;
//        long chatId2 = 2L;
//
//        jdbcClient.sql("INSERT INTO chat (id) VALUES (:chatId)").param("chatId", chatId1).update();
//        jdbcClient.sql("INSERT INTO chat (id) VALUES (:chatId)").param("chatId", chatId2).update();
//        jdbcClient.sql("INSERT INTO chat_link (chat_id, link_id) VALUES (:chatId, :linkId)")
//            .param("chatId", chatId1).param("linkId", existingLinkId).update();
//        jdbcClient.sql("INSERT INTO chat_link (chat_id, link_id) VALUES (:chatId, :linkId)")
//            .param("chatId", chatId2).param("linkId", existingLinkId).update();
//
//        List<Chat> chats = repository.findAllByLinkId(existingLinkId);
//
//        assertThat(chats).isNotNull();
//        assertThat(chats.size()).isEqualTo(2);
//        assertThat(chats).extracting("chatId").containsExactlyInAnyOrder(chatId1, chatId2);
//    }
//
//    @Transactional
//    @Rollback
//    @Test
//    public void findAllByLinkId_shouldReturnEmptyList_whenLinkDoesNotExist() {
//        long nonExistentLinkId = 100L;
//
//        List<Chat> chats = repository.findAllByLinkId(nonExistentLinkId);
//
//        assertThat(chats).isNotNull();
//        assertThat(chats).isEmpty();
//    }
//}
