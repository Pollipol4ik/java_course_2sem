package edu.java.scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.controller.LinkController;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UnsupportedLinkTypeException;
import edu.java.service.LinkService;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkController.class)
public class LinkControllerTest {

    private static final long CHAT_ID = 1;

    @MockBean
    private LinkService linkService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @DisplayName("LinkController#getLinks basic test")
    public void getAllLinks_shouldReturnCorrectResponse_whenAllIsCorrect() {
        long linkId = 2312;
        String link = "test.com";
        Mockito.when(linkService.getAllLinks(CHAT_ID))
            .thenReturn(new ListLinksResponse(List.of(new ResponseLink(linkId, link))));

        mvc.perform(get("/links").header("Tg-Chat-Id", CHAT_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.links[0].id").value(linkId))
            .andExpect(jsonPath("$.links[0].url").value(link));
    }

    @Test
    @SneakyThrows
    @DisplayName("LinkController#getLinks with ChatNotFound exception test")
    public void getAllLinks_shouldReturn404_whenChatNotFound() {
        Mockito.when(linkService.getAllLinks(CHAT_ID)).thenThrow(ChatNotFoundException.class);

        mvc.perform(get("/links").header("Tg-Chat-Id", CHAT_ID))
            .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    @DisplayName("LinkController#addLink basic test")
    public void addLink_shouldReturnCorrectResponse_whenAllIsCorrect() {
        long linkId = 2312;
        String link = "test.com";
        AddLinkRequest addLinkRequest = new AddLinkRequest(link);
        Mockito.when(linkService.addLink(CHAT_ID, addLinkRequest)).thenReturn(new ResponseLink(linkId, link));

        mvc.perform(post("/links")
                .header("Tg-Chat-id", CHAT_ID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(addLinkRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(linkId))
            .andExpect(jsonPath("$.url").value(link));
    }

    @Test
    @SneakyThrows
    @DisplayName("LinkController#addLink with ChatNotFound exception test")
    public void addLink_shouldReturn404_whenChatNotFound() {
        Mockito.when(linkService.addLink(CHAT_ID, new AddLinkRequest(null))).thenThrow(ChatNotFoundException.class);

        mvc.perform(post("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new AddLinkRequest(null))))
            .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    @DisplayName("LinkController#addLink with LinkAlreadyTracked exception test")
    public void addLink_shouldReturn409_whenLinkAlreadyTracked() {
        Mockito.when(linkService.addLink(CHAT_ID, new AddLinkRequest(null)))
            .thenThrow(LinkAlreadyTrackedException.class);

        mvc.perform(post("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new AddLinkRequest(null))))
            .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    @DisplayName("LinkController#addLink with UnsupportedLinkType exception test")
    public void addLink_shouldReturn422_whenLinkHasUnsupportedType() {
        Mockito.when(linkService.addLink(CHAT_ID, new AddLinkRequest(null)))
            .thenThrow(UnsupportedLinkTypeException.class);

        mvc.perform(post("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new AddLinkRequest(null))))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @SneakyThrows
    @DisplayName("LinkController#removeLink basic test")
    public void removeLink_shouldReturnCorrectResponse_whenAllIsCorrect() {
        long linkId = 23;
        ResponseLink response = new ResponseLink(linkId, "test.com");
        Mockito.when(linkService.removeLink(CHAT_ID, new RemoveLinkRequest(linkId))).thenReturn(response);

        mvc.perform(delete("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RemoveLinkRequest(linkId))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(linkId))
            .andExpect(jsonPath("$.url").value(response.link()));
    }

    @Test
    @SneakyThrows
    @DisplayName("LinkController#removeLink with LinkNotFound exception test")
    public void removeLink_shouldReturn404_whenLinkIsNotFound() {
        Mockito.when(linkService.removeLink(CHAT_ID, new RemoveLinkRequest(0)))
            .thenThrow(LinkNotFoundException.class);

        mvc.perform(delete("/links")
                .header("Tg-Chat-Id", CHAT_ID)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new RemoveLinkRequest(0))))
            .andExpect(status().isNotFound());
    }
}
