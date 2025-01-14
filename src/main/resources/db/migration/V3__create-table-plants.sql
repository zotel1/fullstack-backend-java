create table plants (
    id bigint not null auto_increment primary key,
    nombre varchar(100) not null,
    country_id bigint not null,
    cantidad_lecturas int not null,
    alertas_medias int not null,
    alertas_rojas int not null,
    readings_ok boolean default false,
    disabled boolean default false
    constraint fk_plants_country_id foreign key (country_id) references country (id)
);