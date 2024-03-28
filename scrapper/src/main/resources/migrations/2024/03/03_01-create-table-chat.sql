--liquibase formatted sql

--changeset Polina Kuptsova:create-link-and-chat-tables
create table if not exists chat
(
    id bigint not null primary key
);

create table if not exists link
(
    id              bigint not null primary key,
    url             text   not null unique,
    type            text   not null,
    updated_at      timestamp with time zone,
    last_checked_at timestamp with time zone
);

create table if not exists chat_link
(
    chat_id bigint not null,
    link_id bigint not null,
    primary key (chat_id, link_id),
    foreign key (chat_id) references chat (id),
    foreign key (link_id) references link (id)
);
--rollback drop table chat, link, chat_link;
