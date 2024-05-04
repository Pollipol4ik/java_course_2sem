package edu.java.repository.chat_link;

import edu.java.dto.Chat;
import java.util.List;

public interface ChatLinkRepository {

    void add(long linkId, long chatId);

    void remove(long linkId, long chatId);

    void removeByLinkId(long linkId);

    List<Chat> findAllByLinkId(long linkId);
}
