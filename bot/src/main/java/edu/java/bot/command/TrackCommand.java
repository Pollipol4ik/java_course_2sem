package edu.java.bot.command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static edu.java.bot.command.Command.TRACK;
import static edu.java.bot.util.MessagesUtils.HTTPS_PREFIX;
import static edu.java.bot.util.MessagesUtils.HTTP_PREFIX;
import static edu.java.bot.util.MessagesUtils.LINK_IS_TRACKED;
import static edu.java.bot.util.MessagesUtils.LINK_SHOULD_STARTS_WITH_HTTP;
import static edu.java.bot.util.MessagesUtils.TRACK_EXAMPLE;

@Log4j2
@RequiredArgsConstructor
public class TrackCommand extends CommandExecutor {

    private final CommandService service;

    @Override
    protected SendMessage execute(String command, long chatId) {
        if (!isTrackCommand(command)) {
            return executeNext(command, chatId);
        }
        log.info("Command /track has executed");
        return buildMessage(command, chatId);
    }

    private boolean isTrackCommand(String command) {
        return command.startsWith(TRACK.getName());
    }

    private SendMessage buildMessage(String command, long chatId) {
        String[] splitCommand = command.split(" ");
        if (splitCommand.length != 2) {
            return buildTrackExampleMessage(chatId);
        }
        if (!isHttpOrHttpsLink(splitCommand[1])) {
            return buildLinkStartsWithHttpMessage(chatId);
        }
        service.trackLink(chatId, splitCommand[1]);
        return buildLinkIsTrackedMessage(chatId, splitCommand[1]);
    }

    private boolean isHttpOrHttpsLink(String link) {
        return link.startsWith(HTTPS_PREFIX) || link.startsWith(HTTP_PREFIX);
    }

    private SendMessage buildTrackExampleMessage(long chatId) {
        return new SendMessage(chatId, TRACK_EXAMPLE);
    }

    private SendMessage buildLinkStartsWithHttpMessage(long chatId) {
        return new SendMessage(chatId, LINK_SHOULD_STARTS_WITH_HTTP);
    }

    private SendMessage buildLinkIsTrackedMessage(long chatId, String link) {
        return new SendMessage(chatId, LINK_IS_TRACKED.formatted(link));
    }
}
