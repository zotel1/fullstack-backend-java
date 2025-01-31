create table plants (
    id serial primary key,
    nombre varchar(100) not null,
    country_id int not null,
    cantidad_lecturas int not null,
    alertas_medias int not null,
    alertas_rojas int not null,
    readings_ok boolean default false,
    disabled boolean default false,
    created_by int not null,
    created_at timestamp default current_timestamp,
    constraint fk_plants_country_id foreign key (country_id) references country (id),
    constraint fk_plants_created_by foreign key (created_by) references users(user_id)
);