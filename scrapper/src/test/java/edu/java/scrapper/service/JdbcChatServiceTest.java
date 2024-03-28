package edu.java.scrapper.service;

import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.repository.chat.ChatRepository;
import edu.java.service.chat.jdbc.JdbcChatService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JdbcChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private JdbcChatService chatService;

    @Test
    public void registerChat_shouldThrowChatAlreadyRegisteredException() {
        // Arrange
        long chatId = 1L;
        when(chatRepository.doesExist(chatId)).thenReturn(true);

        // Act & Assert
        assertThrows(ChatAlreadyRegisteredException.class, () -> chatService.registerChat(chatId));
        verify(chatRepository, never()).add(anyLong());
    }

    @Test
    public void registerChat_shouldAddChatSuccessfully() {
        // Arrange
        long chatId = 1L;
        when(chatRepository.doesExist(chatId)).thenReturn(false);

        // Act
        chatService.registerChat(chatId);

        // Assert
        verify(chatRepository, times(1)).add(chatId);
    }

    @Test
    public void deleteChat_shouldThrowChatNotFoundException() {
        // Arrange
        long chatId = 1L;
        when(chatRepository.doesExist(chatId)).thenReturn(false);

        // Act & Assert
        assertThrows(ChatNotFoundException.class, () -> chatService.deleteChat(chatId));
        verify(chatRepository, never()).remove(anyLong());
    }

    @Test
    public void deleteChat_shouldRemoveChatSuccessfully() {
        // Arrange
        long chatId = 1L;
        when(chatRepository.doesExist(chatId)).thenReturn(true);

        // Act
        chatService.deleteChat(chatId);

        // Assert
        verify(chatRepository, times(1)).remove(chatId);
    }
}
