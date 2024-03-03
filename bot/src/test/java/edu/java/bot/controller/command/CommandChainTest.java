package edu.java.bot.controller.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandChain;
import edu.java.bot.command.HelpCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.util.MessagesUtils.ERROR_MESSAGE;
import static edu.java.bot.util.MessagesUtils.ONLY_TEXT_TO_SEND;

@ExtendWith(MockitoExtension.class)
public class CommandChainTest {

    @Test
    @DisplayName("CommandChain#execute with non text message test")
    public void executeCommand_shouldReturnCorrectMessage_whenReceiveNonTextMessage() {
        CommandChain commandChain = new CommandChain();

        SendMessage actual = commandChain.executeCommand(null, 1);

        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ONLY_TEXT_TO_SEND);
    }

    @Test
    @DisplayName("CommandChain#execute with  text message test")
    public void executeCommand_shouldCallExecuteMethod_whenReceiveTextMessage() {
        CommandChain commandChain = new CommandChain();
        String command = "/help";
        HelpCommand mock = Mockito.mock(HelpCommand.class);
        long chatId = 1;
        commandChain.registerCommand("/help", mock);

        commandChain.executeCommand(command, chatId);

        Mockito.verify(mock, Mockito.only()).execute(command, chatId);
    }

    @Test
    @DisplayName("CommandChain#execute test")
    public void execute_shouldReturnCorrectMessage_whenCommandIsInvalid() {
        CommandChain commandChain = new CommandChain();
        SendMessage actual = commandChain.executeCommand("wrokoj", 1);

        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(ERROR_MESSAGE);
    }

}
