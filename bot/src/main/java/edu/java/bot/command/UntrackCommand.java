package edu.java.bot.command;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.Link;
import edu.java.bot.service.CommandService;
import edu.java.bot.util.KeyboardBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static edu.java.bot.command.Command.UNTRACK;
import static edu.java.bot.util.MessagesUtils.CHOOSE_LINK_TO_UNTRACK;
import static edu.java.bot.util.MessagesUtils.NO_TRACKED_LINKS;

@Log4j2
@RequiredArgsConstructor
public class UntrackCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isUntrackCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Command /untrack has executed");
        return buildMessage(chatId);
    }

    private boolean isUntrackCommand(String command) {
        return command.equals(UNTRACK.getName());
    }

    private SendMessage buildMessage(long chatId) {
        List<Link> links = service.getAllTrackedLinks(chatId);
        if (links.isEmpty()) {
            return buildNoTrackedLinksMessage(chatId);
        }
        Keyboard keyboard = KeyboardBuilder.createCallbackKeyboard(links);
        return buildChooseLinkToUntrackMessage(chatId, keyboard);
    }

    private SendMessage buildNoTrackedLinksMessage(long chatId) {
        return new SendMessage(chatId, NO_TRACKED_LINKS);
    }

    private SendMessage buildChooseLinkToUntrackMessage(long chatId, Keyboard keyboard) {
        return new SendMessage(chatId, CHOOSE_LINK_TO_UNTRACK).replyMarkup(keyboard);
    }
}
