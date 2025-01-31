create table country (
    id serial primary key,
    name varchar(100) not null unique,
    flag_url varchar(255)
);