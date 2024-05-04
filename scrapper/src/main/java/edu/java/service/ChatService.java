package edu.java.service;

import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final List<Long> chatList = new ArrayList<>();

    public void registerChat(Long chatId) throws ChatAlreadyRegisteredException {
        if (this.chatList.contains(chatId)) {
            throw new ChatAlreadyRegisteredException(chatId);
        }
        this.chatList.add(chatId);
    }

    public void deleteChat(Long chatId) {
        if (!this.chatList.contains(chatId)) {
            throw new ChatNotFoundException(chatId);
        }
        this.chatList.remove(chatId);
    }
}
