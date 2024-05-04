package edu.java.scrapper.repository;

import edu.java.repository.chat.JdbcChatRepository;
import edu.java.scrapper.IntegrationEnvironment;
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
public class JdbcChatRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcClient jdbcClient;
    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    public void add_shouldCorrectlyAddIdInChatTable_whenIdIsNotInTable() {
        //Arrange
        long chatId = 1L;
        //Act
        chatRepository.add(chatId);
        //Assert
        Long idCount = jdbcClient.sql("SELECT COUNT(*) FROM chat WHERE id = :id")
            .param("id", chatId)
            .query(Long.class)
            .single();
        assertThat(idCount).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void remove_shouldDeleteIdFromChatTable_whenIdExists() {
        //Arrange
        long chatId = 1879L;
        jdbcClient.sql("INSERT INTO chat (id) VALUES (:id)")
            .param("id", chatId)
            .update();
        //Act
        chatRepository.remove(chatId);
        //Assert
        Long idCount = jdbcClient.sql("SELECT COUNT(*) FROM chat WHERE id = :id")
            .param("id", chatId)
            .query(Long.class)
            .single();
        assertThat(idCount).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void doesExist_shouldReturnFalse_whenChatIdDoesNotExists() {
        //Arrange
        long chatId = 123L;
        //Act
        boolean inTable = chatRepository.doesExist(chatId);
        //Assert
        assertThat(inTable).isFalse();
    }
}
