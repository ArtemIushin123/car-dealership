create table if not exists outboxes (
    id UUID primary key,
    created_at timestamp default now() not null,
    payload text not null,
    processed boolean default false not null,
    queue_name varchar(255) not null
);