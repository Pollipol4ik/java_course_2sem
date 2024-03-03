package edu.java.bot;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class LinkTrackerBot {

    private final TelegramBot bot;
    private final BotCommand[] commands;
    private final UpdatesListener updatesListener;

    @PostConstruct
    private void start() {
        log.info("Bot has been started");
        bot.execute(new SetMyCommands(commands));
        bot.setUpdatesListener(updatesListener);
    }
}

