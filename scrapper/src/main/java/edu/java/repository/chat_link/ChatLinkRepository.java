package edu.java.repository.chat_link;

import edu.java.dto.ChatLinkResponse;
import edu.java.dto.ResponseLink;
import java.time.OffsetDateTime;
import java.util.List;

public interface ChatLinkRepository {
    List<ChatLinkResponse> findAllFiltered(OffsetDateTime time);

    void add(Long chatId, Long linkId);

    ResponseLink remove(Long chatId, Long linkId);

    boolean isTracked(Long chatId, Long linkId);

    boolean hasChats(Long linkId);
}
