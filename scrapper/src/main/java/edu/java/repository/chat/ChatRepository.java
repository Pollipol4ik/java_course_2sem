package edu.java.repository.chat;

import edu.java.dto.Chat;
import java.util.List;

public interface ChatRepository {
    void add(long chatId);

    void remove(long chatId);

    List<Chat> findAll();

    boolean doesExist(long chatId);

}
