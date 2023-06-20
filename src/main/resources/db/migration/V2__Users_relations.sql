create table follower
(
    uuid             uuid primary key             not null,
    subscriber_uuid  uuid references users (uuid) not null,
    interest_uuid    uuid references users (uuid) not null,
    relations_status smallint                     not null,
    created_at       timestamp                    not null,
    updated_at       timestamp
);

create table friend
(
    uuid             uuid primary key             not null,
    subscriber_uuid  uuid references users (uuid) not null,
    interest_uuid    uuid references users (uuid) not null,
    relations_status smallint                     not null,
    created_at       timestamp                    not null,
    updated_at       timestamp
)