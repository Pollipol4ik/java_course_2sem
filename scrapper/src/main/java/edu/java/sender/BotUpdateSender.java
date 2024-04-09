package edu.java.sender;

import edu.java.client.BotClient;
import edu.java.dto.UpdateLink;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BotUpdateSender implements UpdateSender {
    private final BotClient botClient;

    @Override
    public void sendUpdate(UpdateLink update) {
        botClient.sendUpdate(update);
    }
}
