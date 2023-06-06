create table users
(
    id         serial       not null primary key,
    email      varchar(256),
    first_name varchar(256),
    last_name  varchar(256),
    password   varchar(256) not null,
    role       varchar(256)
);

create table posts
(
    id      serial not null primary key,
    header  varchar(256),
    content varchar(1024),
    image   varchar(256),
    user_id serial not null,
    created_at timestamp,
    updated_at timestamp
);