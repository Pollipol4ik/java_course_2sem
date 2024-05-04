package edu.java.scrapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.controller.LinkController;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.exception.UnsupportedLinkTypeException;
import edu.java.service.LinkService;
import java.net.URI;
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
    //Given
    @MockBean
    private LinkService linkService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @DisplayName("Correct request getAllLinks Test")
    public void getAllLinks_shouldReturnOk_whenRequestIsCorrect() {
        //Arrange
        long linkId = 123;
        URI url = URI.create("google.com");
        Mockito.when(linkService.getAllLinks(1L))
            .thenReturn(new ListLinksResponse(List.of(new ResponseLink(linkId, url))));
        //Act
        var act = mvc.perform(get("/links").header("Tg-Chat-Id", 1L).contentType("application/json"));
        //Assert
        act.andExpect(status().isOk())
            .andExpect(jsonPath("$.links[0].id").value(linkId))
            .andExpect(jsonPath("$.links[0].url").value(url.toString()));
    }

    @Test
    @DisplayName("No header getAllLinks test")
    @SneakyThrows
    public void getAllLinks_shouldReturnBadRequest_whenNoHeader() {
        //Arrange
        long linkId = 123;
        URI url = URI.create("google.com");
        Mockito.when(linkService.getAllLinks(1L))
            .thenReturn(new ListLinksResponse(List.of(new ResponseLink(linkId, url))));
        //Act
        var act = mvc.perform(get("/links").contentType("application/json"));
        //Assert
        act.andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @DisplayName("Wrong header getAllLinks test")
    @SneakyThrows
    public void getAllLinks_shouldReturnBadRequest_whenHeaderIsWrong() {
        //Arrange
        long linkId = 123;
        URI url = URI.create("google.com");
        Mockito.when(linkService.getAllLinks(1L))
            .thenReturn(new ListLinksResponse(List.of(new ResponseLink(linkId, url))));
        //Act
        var act = mvc.perform(get("/links").contentType("application/json").header("Tg-Chat-Id", "asd"));
        //Assert
        act.andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @SneakyThrows
    @DisplayName("Correct request deleteLink test")
    public void deleteLink_shouldReturnOk_whenRequestIsCorrect() {
        //Arrange
        long linkId = 123;
        URI url = URI.create("google.com");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        Mockito.when(linkService.removeLink(1L, removeLinkRequest))
            .thenReturn(new ResponseLink(linkId, url));
        //Act
        var act = mvc
            .perform(delete("/links")
                .header("Tg-Chat-Id", 1L)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(removeLinkRequest)));
        //Assert
        act.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(linkId))
            .andExpect(jsonPath("$.url").value(url.toString()));
    }

    @Test
    @SneakyThrows
    @DisplayName("No header deleteLink test")
    public void deleteLink_shouldReturnBadRequest_whenNoHeader() {
        //Arrange
        long linkId = 123;
        URI url = URI.create("google.com");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        Mockito.when(linkService.removeLink(1L, removeLinkRequest))
            .thenReturn(new ResponseLink(linkId, url));
        //Act
        var act = mvc.perform(delete("/links").contentType("application/json")
            .content(objectMapper.writeValueAsString(removeLinkRequest)));
        //Assert
        act.andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("Wrong header deleteLink test")
    public void deleteLink_shouldReturnBadRequest_whenWrongHeader() {
        //Arrange
        long linkId = 123;
        URI url = URI.create("google.com");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        Mockito.when(linkService.removeLink(1L, removeLinkRequest))
            .thenReturn(new ResponseLink(linkId, url));
        //Act
        var act = mvc.perform(delete("/links").header("Tg-Chat-Id", "a")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(removeLinkRequest)));
        //Assert
        act.andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("No body deleteLink test")
    public void deleteLink_shouldReturnBadRequest_whenNoBody() {
        //Arrange
        long linkId = 123;
        URI url = URI.create("google.com");
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        Mockito.when(linkService.removeLink(1L, removeLinkRequest))
            .thenReturn(new ResponseLink(linkId, url));
        //Act
        var act = mvc
            .perform(delete("/links")
                .header("Tg-Chat-Id", 1L)
                .contentType("application/json"));
        //Assert
        act.andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("Correct request addLink test")
    public void addLink_shouldReturnOk_whenRequestIsCorrect() {
        //Arrange
        long linkId = 123;
        URI url = URI.create("google.com");
        AddLinkRequest addLinkRequest = new AddLinkRequest(url.toString());
        Mockito.when(linkService.addLink(1L, addLinkRequest))
            .thenReturn(new ResponseLink(linkId, url));
        //Act
        var act = mvc.perform(post("/links")
            .header("Tg-Chat-Id", 1L).contentType("application/json")
            .content(objectMapper.writeValueAsString(addLinkRequest)));
        //Assert
        act.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(linkId))
            .andExpect(jsonPath("$.url").value(url.toString()));
    }

    @Test
    @SneakyThrows
    @DisplayName("No header addLink test")
    public void addLink_shouldReturnBadRequest_whenNoHeader() {
        //Arrange
        URI url = URI.create("google.com");
        AddLinkRequest addLinkRequest = new AddLinkRequest(url.toString());
        Mockito.when(linkService.addLink(1L, addLinkRequest))
            .thenThrow(new UnsupportedLinkTypeException(addLinkRequest));
        //Act
        var act = mvc.perform(post("/links").contentType("application/json")
            .content(objectMapper.writeValueAsString(addLinkRequest)));
        //Arrange
        act.andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("Wrong header addLink test")
    public void addLink_shouldReturnBadRequest_whenWrongHeader() {
        //Arrange
        URI url = URI.create("google.com");
        AddLinkRequest addLinkRequest = new AddLinkRequest(url.toString());
        Mockito.when(linkService.addLink(1L, addLinkRequest))
            .thenThrow(new UnsupportedLinkTypeException(addLinkRequest));
        //Act
        var act = mvc.perform(post("/links").contentType("application/json")
            .header("Tg-Chat-Id", "")
            .content(objectMapper.writeValueAsString(addLinkRequest)));
        //Assert
        act.andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("Wrong header addLink test")
    public void addLink_shouldReturnBadRequest_whenNoBody() {
        //Arrange
        URI url = URI.create("google.com");
        AddLinkRequest addLinkRequest = new AddLinkRequest(url.toString());
        Mockito.when(linkService.addLink(1L, addLinkRequest))
            .thenThrow(new UnsupportedLinkTypeException(addLinkRequest));
        //Act
        var act = mvc.perform(post("/links").contentType("application/json").header("Tg-Chat-Id", 1L));
        //Assert
        act.andExpect(status().isBadRequest());
    }

}
