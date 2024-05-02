package edu.java.scrapper.controller;

import edu.java.controller.ChatController;
import edu.java.exception.ChatNotFoundException;
import edu.java.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    private static final long CHAT_ID = 1;

    @MockBean
    private ChatService chatService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Register chat - Success")
    public void registerChat_Success() throws Exception {
        Mockito.doNothing().when(chatService).registerChat(CHAT_ID);

        mockMvc.perform(post("/telegram/chat/{chatId}", CHAT_ID))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Register chat - ChatAlreadyRegisteredException")
    void registerChat_ChatAlreadyRegisteredException() throws Exception {
        mockMvc.perform(post("/telegram/chat/{chatId}", 1))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete chat - Success")
    public void deleteChat_Success() throws Exception {
        Mockito.doNothing().when(chatService).deleteChat(CHAT_ID);

        mockMvc.perform(delete("/telegram/chat/{chatId}", CHAT_ID))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete chat - ChatNotFoundException")
    public void deleteChat_ChatNotFoundException() throws Exception {
        Mockito.doThrow(new ChatNotFoundException(CHAT_ID)).when(chatService).deleteChat(CHAT_ID);

        mockMvc.perform(delete("/telegram/chat/{chatId}", CHAT_ID))
            .andExpect(status().isOk());
    }
}
