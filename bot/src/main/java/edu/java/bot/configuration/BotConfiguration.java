package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.command.CommandChain;
import edu.java.bot.resolver.UpdateCallbackResolver;
import edu.java.bot.resolver.UpdateMessageResolver;
import edu.java.bot.resolver.UpdateResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static edu.java.bot.command.Command.HELP;
import static edu.java.bot.command.Command.LIST;
import static edu.java.bot.command.Command.TRACK;
import static edu.java.bot.command.Command.UNTRACK;

@Configuration
public class BotConfiguration {

    @Bean
    public UpdateResolver updateResolver(ScrapperClient scrapperClient, CommandChain commandChain) {
        return UpdateResolver.link(
            new UpdateMessageResolver(commandChain),
            new UpdateCallbackResolver(scrapperClient)
        );
    }

    @Bean
    public BotCommand[] commands() {
        return new BotCommand[] {
            new BotCommand(TRACK.getName(), TRACK.getDescription()),
            new BotCommand(UNTRACK.getName(), UNTRACK.getDescription()),
            new BotCommand(LIST.getName(), LIST.getDescription()),
            new BotCommand(HELP.getName(), HELP.getDescription())
        };
    }

    @Bean
    public TelegramBot bot(ApplicationConfig applicationConfig) {
        return new TelegramBot(applicationConfig.token());
    }
}
