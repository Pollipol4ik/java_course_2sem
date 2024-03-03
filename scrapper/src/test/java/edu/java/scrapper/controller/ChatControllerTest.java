package edu.java.scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.controller.ChatController;
import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.service.ChatService;
import lombok.SneakyThrows;
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
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @DisplayName("ChatController#registerChat basic test")
    public void registerChat_shouldReturn200_whenAllIsCorrect() {
        Mockito.doNothing().when(chatService).registerChat(CHAT_ID);

        mvc.perform(post("/tg-chat/" + CHAT_ID))
            .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @DisplayName("ChatController#registerChat with ChatAlreadyRegistered exception test")
    public void registerChat_shouldReturn409_whenChatIsAlreadyRegistered() {
        Mockito.doThrow(ChatAlreadyRegisteredException.class).when(chatService).registerChat(CHAT_ID);

        mvc.perform(post("/tg-chat/" + CHAT_ID))
            .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    @DisplayName("ChatController#deleteChat basic test")
    public void deleteChat_shouldReturn200_whenAllIsCorrect() {
        Mockito.doNothing().when(chatService).deleteChat(CHAT_ID);

        mvc.perform(delete("/tg-chat/" + CHAT_ID))
            .andExpect(status().isOk());
    }
}
