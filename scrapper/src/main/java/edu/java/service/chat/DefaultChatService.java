package edu.java.service.chat;

import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import edu.java.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class DefaultChatService implements ChatService {
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public void registerChat(Long chatId) {
        if (chatRepository.doesExist(chatId)) {
            throw new ChatAlreadyRegisteredException(chatId);
        }
        chatRepository.add(chatId);
    }

    @Override
    @Transactional
    public void deleteChat(Long chatId) {
        if (!chatRepository.doesExist(chatId)) {
            throw new ChatNotFoundException(chatId);
        }
        chatRepository.remove(chatId);
    }
}
