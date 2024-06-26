package edu.java.bot.resolver;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.CommandService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static edu.java.bot.util.MessagesUtils.LINK_HAS_BEEN_UNTRACKED;

@RequiredArgsConstructor
@Log4j2
public class UpdateCallbackResolver extends UpdateResolver {

    private final CommandService commandService;
    private static final String FORMAT_ERROR = "Invalid callback data format";

    @Override
    public SendMessage resolve(Update update) {
        if (update.callbackQuery() == null) {
            return resolveNext(update);
        }
        long userId = update.callbackQuery().from().id();
        String callbackData = update.callbackQuery().data();

        processCallback(userId, callbackData);
        log.info("Link has been untracked");
        return new SendMessage(userId, LINK_HAS_BEEN_UNTRACKED);
    }

    private void processCallback(long userId, String callbackData) {
        if (!isValidCallbackData(callbackData)) {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }
        UUID linkId = extractLinkId(callbackData);
        commandService.untrackLink(userId, linkId);
    }

    private boolean isValidCallbackData(String data) {
        return data.startsWith("/untrack:");
    }

    private UUID extractLinkId(String data) {
        String[] parts = data.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException(FORMAT_ERROR);
        }
        return UUID.fromString(parts[1]);
    }
}
