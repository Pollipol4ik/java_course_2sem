package edu.java.bot.resolver;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.RemoveLinkRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static edu.java.bot.util.MessagesUtils.LINK_HAS_BEEN_UNTRACKED;

@RequiredArgsConstructor
@Log4j2
public class UpdateCallbackResolver extends UpdateResolver {

    private final ScrapperClient scrapperClient;

    @Override
    public SendMessage resolve(Update update) {
        if (update.callbackQuery() == null) {
            return resolveNext(update);
        }
        processCallback(update.callbackQuery().from().id(), update.callbackQuery().data());
        log.info("Link has been untracked");
        return new SendMessage(update.callbackQuery().from().id(), LINK_HAS_BEEN_UNTRACKED);
    }

    private void processCallback(long chatId, String data) {
        if (!data.startsWith("/untrack:")) {
            throw new RuntimeException("Invalid callback");
        }
        scrapperClient.untrackLink(chatId, new RemoveLinkRequest(Long.parseLong(data.split(":")[1])));
    }
}
