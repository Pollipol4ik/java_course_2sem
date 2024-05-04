package edu.java.scrapper.service;

import edu.java.dto.Chat;
import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.chat.ChatService;
import jakarta.persistence.EntityManager;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext
public class JpaChatServiceTest extends IntegrationEnvironment {
    @Autowired
    private ChatService chatService;

    @Autowired
    private EntityManager manager;

    @Autowired
    private JdbcClient jdbcClient;

    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }
    @Test
    @Transactional
    @Rollback
    public void registerChat_shouldThrowChatAlreadyRegisteredException_whenIdIsInTable() {
        //Arrange
        long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", chatId)
            .update();
        //Expected
        assertThatThrownBy(() -> chatService.registerChat(chatId)).isInstanceOf(ChatAlreadyRegisteredException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteChat_shouldDeleteIdFromChatTable_whenIdExists() {
        //Arrange
        long chatId = 1879L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", chatId)
            .update();
        //Act
        chatService.deleteChat(chatId);
        manager.flush();
        //Assert
        boolean idCount = jdbcClient.sql("SELECT id FROM chat WHERE id = :chatId")
            .param("chatId", chatId)
            .query(Chat.class)
            .optional()
            .isPresent();
        assertThat(idCount).isEqualTo(false);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteChat_shouldThrowChatNotFoundException_whenIdDoesNotExist() {
        //Arrange
        long chatId = 1879L;
        //Expected
        assertThatThrownBy(() -> chatService.deleteChat(chatId)).isInstanceOf(ChatNotFoundException.class);
    }
}
