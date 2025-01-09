create table lecturas (
id bigint not null auto_increment,
planta_id bigint not null,
pais_id bigint not null,
cantidad_lecturas int not null,
alertas_medias int not null,
alertas rojas int not null,
primary key(id),
constraint fk_lecturas_planta_id foreign key (planta_id) reference planta (id),
constraint fk_lecturas_pais_id foreign key (pais_id) reference pais (id))