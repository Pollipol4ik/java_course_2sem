package edu.java.bot.command;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.ResponseLink;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.util.KeyboardBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static edu.java.bot.command.Command.LIST;
import static edu.java.bot.util.MessagesUtils.NO_TRACKED_LINKS;
import static edu.java.bot.util.MessagesUtils.TRACKED_LINKS;


@Log4j2
@RequiredArgsConstructor
public class ListCommand implements CommandExecutor {

    private final ScrapperClient scrapperClient;

    @Override
    public SendMessage execute(String command, long chatId) {
        log.info("Command /list has executed");
        return buildMessage(chatId);
    }

    @Override
    public String getCommandName() {
        return LIST.getName();
    }

    private SendMessage buildMessage(long chatId) {
        ListLinksResponse listLinksResponse = scrapperClient.getAllTrackedLinks(chatId);
        List<ResponseLink> links = listLinksResponse.links();
        if (links.isEmpty()) {
            return new SendMessage(chatId, NO_TRACKED_LINKS);
        }
        Keyboard keyboard = KeyboardBuilder.buildUrlKeyboard(links);
        return new SendMessage(chatId, TRACKED_LINKS).replyMarkup(keyboard);
    }

}

