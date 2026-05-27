create table if not exists processed_messages (
    id UUID primary key,
    message_id UUID not null unique,
    processed_at timestamp default now() not null
);