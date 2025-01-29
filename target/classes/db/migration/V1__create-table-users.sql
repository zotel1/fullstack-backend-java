create table users (
    user_id bigint not null auto_increment primary key,
    username varchar(100) not null,
    password varchar(255) not null,
    role varchar(50) not null
   );