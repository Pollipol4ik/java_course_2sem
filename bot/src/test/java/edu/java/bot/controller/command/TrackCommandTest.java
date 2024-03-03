package edu.java.bot.controller.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.command.TrackCommand;
import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.service.CommandService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.util.MessagesUtils.LINK_IS_TRACKED;
import static edu.java.bot.util.MessagesUtils.LINK_SHOULD_STARTS_WITH_HTTP;

@ExtendWith(MockitoExtension.class)
public class TrackCommandTest {

    @Mock
    private ScrapperClient scrapperClient;
    @InjectMocks
    private TrackCommand commandExecutor;

    @Test
    @DisplayName("TrackCommandExecutor#execute with correct link test")
    public void execute_shouldReturnCorrectMessage_whenLinkIsCorrect() {
        long chatId = 1;
        String command = "/track https://github.com";

        SendMessage actual = commandExecutor.execute(command, chatId);

        Assertions.assertThat(actual.getParameters().get("text"))
            .isEqualTo(LINK_IS_TRACKED.formatted(command.split(" ")[1]));
        Mockito.verify(scrapperClient).trackLink(chatId, new AddLinkRequest(command.split(" ")[1]));
    }

    @Test
    @DisplayName("TrackCommandExecutor#execute with incorrect link test")
    public void execute_shouldReturnCorrectMessage_whenLinkIsIncorrect() {
        long chatId = 1;
        String command = "/track github.com";

        SendMessage actual = commandExecutor.execute(command, chatId);

        Assertions.assertThat(actual.getParameters().get("text"))
            .isEqualTo(LINK_SHOULD_STARTS_WITH_HTTP);
    }
}
