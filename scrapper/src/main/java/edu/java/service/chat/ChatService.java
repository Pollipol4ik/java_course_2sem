package edu.java.service.chat;

import edu.java.exception.ChatAlreadyRegisteredException;
import edu.java.exception.ChatNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {

    void registerChat(Long chatId);
    void deleteChat(Long chatId);
}
