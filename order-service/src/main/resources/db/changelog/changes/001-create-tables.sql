create table if not exists users(
    id UUID primary key,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,
    removed boolean default false not null,
    name varchar(255) not null,
    role varchar(255) not null
);

create table if not exists orders(
    id UUID primary key,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,
    removed boolean default false not null,
    client_id UUID not null references users(id),
    manager_id UUID not null references users(id),
    order_status varchar(50) not null
);

create table if not exists stock_orders(
    id UUID primary key references orders(id),
    car_id UUID not null
);

create table if not exists custom_orders(
    id UUID primary key references orders(id),
    car_model_id UUID not null
);

create table if not exists custom_order_selected_components(
    custom_order_id UUID not null references custom_orders(id),
    component_option_id UUID not null
);

create table if not exists test_drives(
    id UUID primary key,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,
    removed boolean default false not null,
    client_id UUID not null references users(id),
    car_id UUID not null,
    start_time timestamp not null,
    test_drive_status varchar(50) not null
);

create table if not exists outbox (
    id UUID primary key,
    created_at timestamp default now() not null,
    payload text not null,
    processed boolean default false not null,
    queue_name varchar(255) not null
);