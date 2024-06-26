package edu.java.bot.command;


import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.Link;
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
import static edu.java.bot.command.Command.UNTRACK;
import static edu.java.bot.util.MessagesUtils.CHOOSE_LINK_TO_UNTRACK;
import static edu.java.bot.util.MessagesUtils.NO_TRACKED_LINKS;

@ExtendWith(MockitoExtension.class)
public class UntrackCommandTest {

    @Mock
    private CommandService linkService;
    @InjectMocks
    private UntrackCommand commandExecutor;

    @Test
    @DisplayName("UntrackCommandExecutor#execute with tracked links test")
    public void execute_shouldReturnCorrectMessage_whenTrackedLinksExist() {
        long chatId = 1;
        Mockito.when(linkService.getAllTrackedLinks(chatId)).thenReturn(List.of(
            new Link(
                UUID.randomUUID(),
                "https://github.com/Pollipol4ik"
            ),
            new Link(
                UUID.randomUUID(),
                "https://stackoverflow.com/questions/77425606/mock-contract-allowance-from-ethers-js-using-jest"
            )
        ));

        SendMessage actual = commandExecutor.execute(UNTRACK.getName(), chatId);

        Assertions.assertThat(actual.getParameters().get("text"))
            .isEqualTo(CHOOSE_LINK_TO_UNTRACK);
    }

    @Test
    @DisplayName("UntrackCommandExecutor#execute with no tracked links test")
    public void execute_shouldReturnCorrectMessage_whenNoTrackedLinks() {
        long chatId = 1;
        Mockito.when(linkService.getAllTrackedLinks(chatId)).thenReturn(Collections.emptyList());

        SendMessage actual = commandExecutor.execute(UNTRACK.getName(), chatId);

        Assertions.assertThat(actual.getParameters().get("text"))
            .isEqualTo(NO_TRACKED_LINKS);
    }
}
