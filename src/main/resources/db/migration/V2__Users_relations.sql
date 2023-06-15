create table relations
(
    uuid             uuid primary key             not null,
    subscriber_uuid  uuid references users (uuid) not null,
    interest_uuid    uuid references users (uuid) not null,
    relations_status smallint                     not null
)