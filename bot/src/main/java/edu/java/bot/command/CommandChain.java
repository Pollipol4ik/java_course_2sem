package edu.java.bot.command;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.util.MessagesUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;


@Component
@Log4j2
@RequiredArgsConstructor
public class CommandChain {

    private final Map<String, CommandExecutor> executors = new HashMap<>();

    public void registerCommand(String commandName, CommandExecutor executor) {
        executors.put(commandName, executor);
    }

    public SendMessage executeCommand(String command, long chatId) {
        if (command == null) {
            log.info("Null message has been received");
            return new SendMessage(chatId, MessagesUtils.ONLY_TEXT_TO_SEND);
        }

        String[] splitCommand = command.split(" ");
        CommandExecutor executor = executors.getOrDefault(splitCommand[0], null);

        if (executor == null) {
            return new SendMessage(chatId, MessagesUtils.ERROR_MESSAGE).parseMode(ParseMode.HTML);
        }

        return executor.execute(command, chatId);
    }
}
