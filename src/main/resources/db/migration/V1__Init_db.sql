create table users
(
    id         serial       not null primary key,
    email      varchar(256) unique not null,
    first_name varchar(256) not null,
    last_name  varchar(256) not null,
    password   varchar(256) not null,
    role       varchar(256) not null
);

create table posts
(
    id      serial not null primary key,
    header  varchar(256),
    content varchar(1024),
    image   varchar(256),
    user_id serial references users (id) not null,
    created_at timestamp not null ,
    updated_at timestamp
);