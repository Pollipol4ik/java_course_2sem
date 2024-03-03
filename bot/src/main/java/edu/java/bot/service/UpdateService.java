package edu.java.bot.service;

import edu.java.bot.dto.UpdateLink;
import edu.java.bot.message_sender.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateService {

    private final Sender sender;

    public void sendUpdate(UpdateLink linkUpdate) {

    }
}
