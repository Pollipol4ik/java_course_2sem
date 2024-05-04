package edu.java.bot.resolver;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.CommandChain;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;


@Log4j2
@Component
public class UpdateMessageResolver extends UpdateResolver {

    private final CommandChain commandChain;
    private final Counter counter;

    public UpdateMessageResolver(CommandChain commandChain, MeterRegistry meterRegistry) {
        this.commandChain = commandChain;
        counter = Counter.builder("messages_processed")
            .description("Total number of processed messages")
            .register(meterRegistry);
    }

    @Override
    public SendMessage resolve(Update update) {
        counter.increment();
        if (update.message() == null) {
            return resolveNext(update);
        }
        return commandChain.executeCommand(update.message().text(), update.message().chat().id());
    }
}
