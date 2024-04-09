package edu.java.repository.chat;

import edu.java.dto.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private static final String PARAM_CHAT_ID = "chatId";
    private final JdbcClient jdbcClient;

    @Override
    public void add(long chatId) {
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:" + PARAM_CHAT_ID + ")")
            .param(PARAM_CHAT_ID, chatId)
            .update();
    }

    @Override
    public void remove(long chatId) {
        jdbcClient.sql("DELETE FROM chat WHERE id = :" + PARAM_CHAT_ID)
            .param(PARAM_CHAT_ID, chatId)
            .update();
    }

    @Override
    public List<Chat> findAll() {
        return jdbcClient.sql("SELECT id FROM chat")
            .query(Chat.class)
            .list();
    }

    @Override
    public boolean doesExist(long chatId) {
        return jdbcClient.sql("SELECT id AS chat_id FROM chat WHERE id = :" + PARAM_CHAT_ID)
            .param(PARAM_CHAT_ID, chatId)
            .query(Chat.class)
            .optional()
            .isPresent();
    }
}
