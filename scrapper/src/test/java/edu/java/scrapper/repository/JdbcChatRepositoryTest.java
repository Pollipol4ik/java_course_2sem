//package edu.java.scrapper.repository;
//
//import edu.java.repository.chat.JdbcChatRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.simple.JdbcClient;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class JdbcChatRepositoryTest {
//
//    @Autowired
//    private JdbcClient jdbcClient;
//
//    @Autowired
//    private JdbcChatRepository jdbcChatRepository;
//
////    @Test
////    @Transactional
////    @Rollback
////    public void add_shouldCorrectlyAddIdInChatTable_whenIdIsNotInTable() {
////        // Arrange
////        long chatId = 1L;
////
////        // Act
////        jdbcChatRepository.add(chatId);
////
////        // Assert
////        boolean idExists = jdbcClient.sql("SELECT id FROM chat WHERE id = :chatId")
////            .param("chatId", chatId)
////            .query(Chat.class)
////            .optional()
////            .isPresent();
////        assertThat(idExists).isTrue();
////    }
////
////
////    @Test
////    @Transactional
////    @Rollback
////    public void remove_shouldDeleteIdFromChatTable_whenIdExists() {
////        // Arrange
////        long chatId = 1879L;
////        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
////            .param("chatId", chatId)
////            .update();
////
////        // Act
////        jdbcChatRepository.remove(chatId);
////
////        // Assert
////        boolean idExists = jdbcClient.sql("SELECT id FROM chat WHERE id = :chatId")
////            .param("chatId", chatId)
////            .query(Chat.class)
////            .optional()
////            .isPresent();
////        assertThat(idExists).isFalse();
////    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void doesExist_shouldReturnTrue_whenChatIdExists() {
//        // Arrange
//        long chatId = 1879L;
//        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
//            .param("chatId", chatId)
//            .update();
//
//        // Act
//        boolean exists = jdbcChatRepository.doesExist(chatId);
//
//        // Assert
//        assertThat(exists).isTrue();
//    }
//
////    @Test
////    @Transactional
////    @Rollback
////    public void doesExist_shouldReturnFalse_whenChatIdDoesNotExist() {
////        // Arrange
////        long chatId = 123L;
////
////        // Act
////        boolean exists = jdbcChatRepository.doesExist(chatId);
////
////        // Assert
////        assertThat(exists).isFalse();
////    }
//}
