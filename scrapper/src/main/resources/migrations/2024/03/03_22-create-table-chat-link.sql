--liquibase formatted sql

--changeset Polina Kuptsova:create-link-and-chat-tables-modify
CREATE SEQUENCE link_id_seq;
ALTER TABLE link
    ALTER COLUMN id SET NOT NULL;
ALTER TABLE link
    ALTER COLUMN id SET DEFAULT nextval('link_id_seq');
ALTER SEQUENCE link_id_seq OWNED BY link.id;
