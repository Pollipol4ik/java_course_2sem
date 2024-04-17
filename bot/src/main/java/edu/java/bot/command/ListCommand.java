package edu.java.bot.command;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.Link;
import edu.java.bot.service.CommandService;
import edu.java.bot.util.KeyboardBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static edu.java.bot.command.Command.LIST;
import static edu.java.bot.util.MessagesUtils.NO_TRACKED_LINKS;
import static edu.java.bot.util.MessagesUtils.TRACKED_LINKS;

@Log4j2
@RequiredArgsConstructor
public class ListCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isListCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Command /list has executed");
        return buildMessage(chatId);
    }

    private boolean isListCommand(String command) {
        return LIST.getName().equals(command);
    }

    private SendMessage buildMessage(long chatId) {
        List<Link> links = service.getAllTrackedLinks(chatId);
        if (links.isEmpty()) {
            return buildNoTrackedLinksMessage(chatId);
        }
        Keyboard keyboard = KeyboardBuilder.createUrlKeyboard(links);
        return buildTrackedLinksMessage(chatId, keyboard);
    }

    private SendMessage buildNoTrackedLinksMessage(long chatId) {
        return new SendMessage(chatId, NO_TRACKED_LINKS);
    }

    private SendMessage buildTrackedLinksMessage(long chatId, Keyboard keyboard) {
        return new SendMessage(chatId, TRACKED_LINKS).replyMarkup(keyboard);
    }
}
