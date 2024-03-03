package edu.java.bot.service;

import edu.java.bot.dto.UpdateLink;
import edu.java.bot.message_sender.TelegramMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateService {

    private final TelegramMessageSender sender;

    public void sendUpdate(UpdateLink linkUpdate) {

    }
}
