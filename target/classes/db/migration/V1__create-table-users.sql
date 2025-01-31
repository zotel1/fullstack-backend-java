create table users (
    user_id serial primary key,
    username varchar(100) not null unique,
    password varchar(255) not null,
    role varchar(50) not null
   );