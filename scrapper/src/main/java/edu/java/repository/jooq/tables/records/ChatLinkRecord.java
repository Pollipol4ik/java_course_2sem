/*
 * This file is generated by jOOQ.
 */

package edu.java.repository.jooq.tables.records;

import edu.java.repository.jooq.tables.ChatLink;
import java.beans.ConstructorProperties;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class ChatLinkRecord extends UpdatableRecordImpl<ChatLinkRecord> implements Record2<Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Create a detached ChatLinkRecord
     */
    public ChatLinkRecord() {
        super(ChatLink.CHAT_LINK);
    }

    /**
     * Create a detached, initialised ChatLinkRecord
     */
    @ConstructorProperties({"chatId", "linkId"})
    public ChatLinkRecord(@NotNull Long chatId, @NotNull Long linkId) {
        super(ChatLink.CHAT_LINK);

        setChatId(chatId);
        setLinkId(linkId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ChatLinkRecord
     */
    public ChatLinkRecord(edu.java.repository.jooq.tables.pojos.ChatLink value) {
        super(ChatLink.CHAT_LINK);

        if (value != null) {
            setChatId(value.getChatId());
            setLinkId(value.getLinkId());
            resetChangedOnNotNull();
        }
    }

    /**
     * Getter for <code>public.chat_link.chat_id</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getChatId() {
        return (Long) get(0);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>public.chat_link.chat_id</code>.
     */
    public void setChatId(@NotNull Long value) {
        set(0, value);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>public.chat_link.link_id</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getLinkId() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>public.chat_link.link_id</code>.
     */
    public void setLinkId(@NotNull Long value) {
        set(1, value);
    }

    @Override
    @NotNull
    public Record2<Long, Long> key() {
        return (Record2) super.key();
    }

    @Override
    @NotNull
    public Row2<Long, Long> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row2<Long, Long> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return ChatLink.CHAT_LINK.CHAT_ID;
    }

    @Override
    @NotNull
    public Field<Long> field2() {
        return ChatLink.CHAT_LINK.LINK_ID;
    }

    @Override
    @NotNull
    public Long component1() {
        return getChatId();
    }

    @Override
    @NotNull
    public Long component2() {
        return getLinkId();
    }

    @Override
    @NotNull
    public Long value1() {
        return getChatId();
    }

    @Override
    @NotNull
    public Long value2() {
        return getLinkId();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public ChatLinkRecord value1(@NotNull Long value) {
        setChatId(value);
        return this;
    }

    @Override
    @NotNull
    public ChatLinkRecord value2(@NotNull Long value) {
        setLinkId(value);
        return this;
    }

    @Override
    @NotNull
    public ChatLinkRecord values(@NotNull Long value1, @NotNull Long value2) {
        value1(value1);
        value2(value2);
        return this;
    }
}
