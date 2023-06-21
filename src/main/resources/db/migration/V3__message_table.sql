create table messages
(
    uuid         uuid                         not null primary key,
    from_uuid    uuid references users (uuid) not null,
    to_uuid      uuid references users (uuid) not null,
    message_text varchar(65536),
    created_at   timestamp                    not null,
    updated_at   timestamp
);