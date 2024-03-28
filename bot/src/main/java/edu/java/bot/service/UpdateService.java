package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.UpdateLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateService {

    private final TelegramMessageSender sender;

    public void sendUpdate(UpdateLink linkUpdate) {
        for (Long chatId : linkUpdate.tgChatIds()) {
            sender.sendMessage(new SendMessage(chatId, linkUpdate.link() + " - " + linkUpdate.description()));
        }
    }
}
