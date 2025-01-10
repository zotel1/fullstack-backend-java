create table readings (
    id bigint not null auto_increment,
    planta_id bigint not null,
    pais_id bigint not null,
    cantidad_lecturas int not null,
    alertas_medias int not null,
    alertas_rojas int not null,
    primary key (id),
    constraint fk_readings_planta_id foreign key (planta_id) references plants (id),
    constraint fk_readings_pais_id foreign key (pais_id) references pais (id)
);
