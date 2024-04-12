package edu.java.bot.resolver;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class UpdateResolver {

    private UpdateResolver next;

    public static UpdateResolver link(UpdateResolver first, UpdateResolver... chain) {
        UpdateResolver head = first;
        for (UpdateResolver resolver : chain) {
            head.next = resolver;
            head = resolver;
        }
        return first;
    }

    public abstract SendMessage resolve(Update update);

    protected SendMessage resolveNext(Update update) {
        if (next == null) {
            throw new RuntimeException("No next resolver available");
        }
        return next.resolve(update);
    }
}
