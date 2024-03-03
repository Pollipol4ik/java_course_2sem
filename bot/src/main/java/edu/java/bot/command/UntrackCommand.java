package edu.java.bot.command;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.ResponseLink;
import edu.java.bot.util.KeyboardBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import static edu.java.bot.command.Command.UNTRACK;
import static edu.java.bot.util.MessagesUtils.CHOOSE_LINK_TO_UNTRACK;
import static edu.java.bot.util.MessagesUtils.NO_TRACKED_LINKS;

@Log4j2
@RequiredArgsConstructor
@Component
public class UntrackCommand implements CommandExecutor {

    private final ScrapperClient scrapperClient;

    @Override
    public SendMessage execute(String command, long chatId) {
        log.info("Command /untrack has executed");
        return buildMessage(chatId);
    }

    @Override
    public String getCommandName() {
        return UNTRACK.getName();
    }

    private SendMessage buildMessage(long chatId) {
        ListLinksResponse listLinksResponse = scrapperClient.getAllTrackedLinks(chatId);
        List<ResponseLink> links = listLinksResponse.links();
        if (links.isEmpty()) {
            return new SendMessage(chatId, NO_TRACKED_LINKS);
        }
        Keyboard keyboard = KeyboardBuilder.buildCallbackKeyboard(links);
        return new SendMessage(chatId, CHOOSE_LINK_TO_UNTRACK).replyMarkup(keyboard);
    }
}
