package edu.java.bot.util;


import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import edu.java.bot.dto.ResponseLink;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KeyboardBuilder {

    public static Keyboard buildUrlKeyboard(List<ResponseLink> links) {
        return new InlineKeyboardMarkup(
            links.stream()
                .map(link -> new InlineKeyboardButton(link.link()).url(link.link()))
                .map(button -> new InlineKeyboardButton[] {button})
                .toArray(InlineKeyboardButton[][]::new)
        );
    }

    public static Keyboard buildCallbackKeyboard(List<ResponseLink> links) {
        return new InlineKeyboardMarkup(
            links.stream()
                .map(link -> new InlineKeyboardButton(link.link()).callbackData("/untrack:" + link.linkId()))
                .map(button -> new InlineKeyboardButton[] {button})
                .toArray(InlineKeyboardButton[][]::new)
        );
    }
}
