create table plants (
    id bigint not null auto_increment,
    nombre varchar(100) not null,
    pais_id bigint not null, -- Nueva columna para la relación con `pais`
    primary key (id),
    constraint fk_plants_pais_id foreign key (pais_id) references pais (id)
);