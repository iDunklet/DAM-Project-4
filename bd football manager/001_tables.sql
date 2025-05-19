create database fotball;

use fotball;

create table ciutats (
	id int auto_increment,
    nom varchar (45) not null,
    constraint pk_ciutat primary key (id)
    );

create table persones (
	id int not null auto_increment,
    nom varchar (45) not null,
    cognoms varchar (45) not null,
    data_naixement date not null,
    nivell_motivacio int not null,
    sou float not null,
    tipus_persona varchar (45) not null,
    constraint pk_persones primary key (id)
    );

create table posicions (
	id int not null auto_increment,
    posicio varchar (45) not null,
    constraint pk_posicions primary key (id)
    );

create table estadis (
	id int not null auto_increment,
    nom varchar (45) not null,
    num_espectadors int not null,
    constraint pk_estadis primary key (id)
    );

create table lligues (
	id int not null auto_increment,
    nom varchar (45) not null,
    temporada year not null,
    constraint pk_lligues primary key (id)
    );

create table entrenadors (
	persones_id int not null,
    num_tornejos int not null,
    es_selecionador tinyint not null,
    constraint pk_entrenadors primary key (persones_id),
    constraint fk_entrenadors_persones foreign key (persones_id) references persones (id)
    );

create table equips (
	id int not null auto_increment,
    nom varchar (45) not null,
    any_fundacio int not null,
    nom_president varchar (45) default null,
    ciutats_id int not null,
    estadis_id int not null,
    filial_equips_id int default null,
    constraint pk_equips primary key (id),
    key fk_equips_ciutats1_idx (ciutats_id),
    key fk_equips_estadis1_idx (estadis_id),
    key fk_equips_equips1_idx (filial_equips_id),
    constraint fk_equips_ciutats1 foreign key (ciutats_id) references ciutats (id),
	constraint fk_equips_equips1 foreign key (filial_equips_id) references equips (id),
	constraint fk_equips_estadis1 foreign key (estadis_id) references estadis (id)
    );

create table entrenar_equips (
	data_fitxatge date not null,
    entrenadors_id int not null,
    equips_id int not null,
    data_baixa date default null,
    constraint pk_entrenar_equips primary key (data_fitxatge, entrenadors_id),
    key fk_equips_has_entrenadors_entrenadors1_idx (entrenadors_id),
    key fk_equips_has_entrenadors_equips1_idx (equips_id),
    constraint fk_equips_has_entrenadors_entrenadors1 foreign key (entrenadors_id) references entrenadors (persones_id),
    constraint fk_equips_has_entrenadors_equips1 foreign key (equips_id) references equips (id)
    );

create table jornades (
	id int not null auto_increment,
    jornada int not null,
    data1 date not null,
    lligues_id int not null,
    constraint pk_jornades primary key (id),
	key fk_partits_lligues1_idx (lligues_id),
	constraint fk_partits_lligues1 foreign key (lligues_id) references lligues (id)
    );
    
create table jugadors (
	persones_id int not null,
    dorsal int not null,
    qualitat int not null,
    posicions_id int not null,
    constraint pk_jugadors primary key (persones_id),
    key fk_jugadors_posicions1_idx (posicions_id),
	constraint fk_jugadors_persones1 foreign key (persones_id) references persones (id),
	constraint fk_jugadors_posicions1 foreign key (posicions_id) references posicions (id)
    );
    
create table jugadors_equips (
	data_fitxatge date not null,
    jugadors_id int not null,
    equips_id int not null,
    data_baixa date default null,
    constraint pk_jugadors_equips primary key (data_fitxatge, jugadors_id),
	key fk_jugadors_has_equips_equips1_idx (equips_id),
	key fk_jugadors_has_equips_jugadors1_idx (jugadors_id),
    constraint fk_jugadors_has_equips_equips1 foreign key (equips_id) references equips (id),
	constraint fk_jugadors_has_equips_jugadors1 foreign key (jugadors_id) references jugadors (persones_id)
    );

    
create table participar_lligues (
	equips_id int not null,
    lligues_id int not null,
    constraint pk_participar_lligues primary key (equips_id, lligues_id),
    key fk_equips_has_lligues_lligues1_idx (lligues_id),
	KEY fk_equips_has_lligues_equips1_idx (equips_id),
	constraint fk_equips_has_lligues_equips1 foreign key (equips_id) references equips (id),
	constraint fk_equips_has_lligues_lligues1 foreign key (lligues_id) references lligues (id)
    );
    
create table partits (
	id int not null auto_increment,
    gols_local int not null,
    gols_visitant int not null,
    punts_local int not null,
    punts_visitant int not null,
    jornades_id int not null,
    equips_id_local int not null,
    equips_id_visitant int not null,
    constraint pk_partits primary key (id),
    UNIQUE KEY uk_partits (equips_id_local,equips_id_visitant,jornades_id),
	KEY fk_partits_equips1_idx (equips_id_local),
	KEY fk_partits_equips2_idx (equips_id_visitant),
	KEY fk_partits_jornades1_idx (jornades_id),
	CONSTRAINT fk_partits_equips1 FOREIGN KEY (equips_id_local) REFERENCES equips (id),
	CONSTRAINT fk_partits_equips2 FOREIGN KEY (equips_id_visitant) REFERENCES equips (id),
	CONSTRAINT fk_partits_jornades1 FOREIGN KEY (jornades_id) REFERENCES jornades (id)
  );
  
create table partits_gols (
	partits_id int not null,
    jugadors_id int not null,
    minut int not null,
    es_penal tinyint not null default '0',
    constraint pk_partit_gols primary key (partits_id, jugadors_id, minut),
    KEY fk_partits_has_jugadors_jugadors1_idx (jugadors_id),
	KEY fk_partits_has_jugadors_partits1_idx (partits_id),
	CONSTRAINT fk_partits_has_jugadors_jugadors1 FOREIGN KEY (jugadors_id) REFERENCES jugadors (persones_id),
	CONSTRAINT fk_partits_has_jugadors_partits1 FOREIGN KEY (partits_id) REFERENCES partits (id)
    );