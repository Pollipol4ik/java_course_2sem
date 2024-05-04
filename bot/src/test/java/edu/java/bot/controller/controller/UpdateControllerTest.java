package edu.java.bot.controller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.controller.UpdateController;
import edu.java.bot.dto.UpdateLink;
import edu.java.bot.service.UpdateService;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateController.class)
public class UpdateControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UpdateService updateService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    public void sendUpdate_shouldReturn200_whenAllIsOk() {
        UpdateLink linkUpdate = new UpdateLink(2, "1", "all", List.of(1L));
        Mockito.doNothing().when(updateService).updateLink(linkUpdate);

        mvc.perform(post("/updates")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(linkUpdate)))
            .andExpect(status().isOk());
    }
}
