create table if not exists car_models(
    id UUID primary key,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,
    removed boolean default false not null,
    price integer not null,
    brand varchar(255) not null,
    model varchar(255) not null,
    body_type varchar(50) not null,
    fuel_type varchar(50) not null,
    engine_power integer not null,
    engine_capacity integer not null,
    drive_type varchar(50) not null
);

create table if not exists component_options(
    id UUID primary key,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,
    removed boolean default false not null,
    name varchar(255) not null,
    component_type varchar(50) not null,
    price_delta integer not null
);

create table if not exists car_model_base_components(
    car_model_id UUID not null references car_models(id),
    component_option_id UUID not null references component_options(id)
);

create table if not exists cars(
    id UUID primary key,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,
    removed boolean default false not null,
    car_model_id UUID not null references car_models(id),
    color varchar(50) not null,
    status varchar(50) not null
);

create table if not exists component_option_compatible_models(
    component_option_id UUID not null references component_options(id),
    car_model_id UUID not null references car_models(id)
);

create table if not exists assembly_orders(
    id UUID primary key,
    created_at timestamp default now() not null,
    updated_at timestamp default now() not null,
    removed boolean default false not null,
    source_order_id UUID not null,
    status varchar(50) not null
);

create table if not exists custom_assembly_orders(
    id UUID primary key references assembly_orders(id),
    car_model_id UUID not null references car_models(id)
);

create table if not exists stock_assembly_orders(
    id UUID primary key references assembly_orders(id),
    car_id UUID not null references cars(id)
);

create table if not exists custom_assembly_order_components(
    custom_order_id UUID not null references custom_assembly_orders(id),
    component_option_id UUID not null references component_options(id)
);