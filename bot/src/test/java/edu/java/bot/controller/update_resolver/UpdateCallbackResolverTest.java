package edu.java.bot.controller.update_resolver;

import com.google.gson.Gson;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.RemoveLinkRequest;
import edu.java.bot.resolver.UpdateCallbackResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static edu.java.bot.util.MessagesUtils.LINK_HAS_BEEN_UNTRACKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class UpdateCallbackResolverTest {

    private final Gson gson = new Gson();

    @Mock
    private ScrapperClient scrapperClient;

    @InjectMocks
    private UpdateCallbackResolver callbackUpdateResolver;

    @Test
    @DisplayName("CallbackUpdateResolver#resolve with valid Callback test")
    public void resolve_shouldReturnCorrectSendMessage_whenCallBackIsValid() {
        long chatId = 123456789L;
        long linkId = 2;
        Update update = getValidUpdate(chatId, String.valueOf(linkId));

        SendMessage result = callbackUpdateResolver.resolve(update);

        Mockito.verify(scrapperClient, times(1)).removeLink(chatId, new RemoveLinkRequest(linkId));
        assertThat(result.getParameters().get("chat_id")).isEqualTo(chatId);
        assertThat(result.getParameters().get("text")).isEqualTo(LINK_HAS_BEEN_UNTRACKED);
    }

    @Test
    @DisplayName("CallbackUpdateResolver#resolve with invalid Callback test")
    public void resolve_shouldThrowException_whenCallbackIsInvalid() {
        Update update = getInvalidUpdate();
        assertThatThrownBy(() -> callbackUpdateResolver.resolve(update)).isInstanceOf(RuntimeException.class);
    }

    private Update getValidUpdate(long chatId, String linkId) {
        return gson.fromJson("""
            {
                "callback_query": {
                    "from": {
                        "id": %d
                    },
                    "data" : "/untrack:%s"
                }
            }
            """.formatted(chatId, linkId), Update.class
        );
    }

    private Update getInvalidUpdate() {
        return gson.fromJson("""
            {
                "callback_query": {
                    "data" : "/:"
                }
            }
            """, Update.class
        );
    }
}
