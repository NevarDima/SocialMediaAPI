create table posts (
                       id serial not null primary key,
                       header char(256),
                       content char(1024),
                       image char(256)
);