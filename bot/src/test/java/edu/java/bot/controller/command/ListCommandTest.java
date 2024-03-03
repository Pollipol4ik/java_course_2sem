package edu.java.bot.controller.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.command.ListCommand;
import edu.java.bot.dto.Link;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.ResponseLink;
import edu.java.bot.service.CommandService;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.command.Command.LIST;
import static edu.java.bot.util.MessagesUtils.NO_TRACKED_LINKS;
import static edu.java.bot.util.MessagesUtils.TRACKED_LINKS;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {

    @Mock
    private ScrapperClient scrapperClient;
    @InjectMocks
    private ListCommand commandExecutor;

    @Test
    @DisplayName("ListCommandExecutor#execute with tracked links test")
    public void execute_shouldReturnCorrectMessage_whenTrackedLinksExist() {
        long chatId = 1;
        Mockito.when(scrapperClient.getAllTrackedLinks(chatId)).thenReturn(new ListLinksResponse(List.of(
            new ResponseLink(
                1,
                "https://github.com/Pollipol4ik"
            ),
            new ResponseLink(
                2,
                "https://stackoverflow.com/questions/77425606/mock-contract-allowance-from-ethers-js-using-jest"
            )
        )));

        SendMessage actual = commandExecutor.execute(LIST.getName(), chatId);

        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(TRACKED_LINKS);
    }

    @Test
    @DisplayName("ListCommandExecutor#execute with no tracked links test")
    public void execute_shouldReturnCorrectMessage_whenNoTrackedLinks() {
        long chatId = 1;
        Mockito.when(scrapperClient.getAllTrackedLinks(chatId))
            .thenReturn(new ListLinksResponse(Collections.emptyList()));

        SendMessage actual = commandExecutor.execute(LIST.getName(), chatId);

        Assertions.assertThat(actual.getParameters().get("text"))
            .isEqualTo(NO_TRACKED_LINKS);
    }
}
