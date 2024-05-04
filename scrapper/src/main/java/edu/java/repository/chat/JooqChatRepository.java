package edu.java.repository.chat;

import edu.java.dto.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import static edu.java.repository.jooq.tables.Chat.CHAT;

@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext context;

    @Override
    public void add(long chatId) {
        context.insertInto(CHAT, CHAT.ID).values(chatId).execute();
    }

    @Override
    public void remove(long chatId) {
        context.deleteFrom(CHAT).where(CHAT.ID.eq(chatId)).execute();
    }

    @Override
    public List<Chat> findAll() {
        return context.selectFrom(CHAT).fetchInto(Chat.class);
    }

    @Override
    public boolean doesExist(long chatId) {
        Record result = context.select().from(CHAT).where(CHAT.ID.eq(chatId)).fetchOne();
        return result != null;
    }
}
