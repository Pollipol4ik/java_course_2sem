package edu.java.bot.controller;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotUpdatesListener;
import edu.java.bot.resolver.UpdateMessageResolver;
import edu.java.bot.service.TelegramMessageSender;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BotUpdatesListenerTest {

    @Mock
    private UpdateMessageResolver updateResolver;
    @Mock
    private TelegramMessageSender messageSender;
    @InjectMocks
    private BotUpdatesListener listener;

    @Test
    @DisplayName("BotUpdatesListener#process test")
    public void process_shouldCallSendMessageMethod_whenNewUpdateRecieved() {
        List<Update> updates = List.of(new Update());
        SendMessage message = new SendMessage(1, "test");
        Mockito.when(updateResolver.resolve(updates.get(0))).thenReturn(message);

        listener.process(List.of(new Update()));

        Mockito.verify(messageSender).sendMessage(message);
    }
}
