create table plants (
    id bigint not null auto_increment primary key,
    nombre varchar(100) not null,
    pais_id bigint not null,
    cantidad_lecturas int not null,
    alertas_medias int not null,
    alertas_rojas int not null
    constraint fk_plants_pais_id foreign key (pais_id) references pais (id)
);