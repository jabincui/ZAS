create table if not exists authority (
	id varchar(40) not null unique,
    ms_id varchar(40) not null,
    url_id varchar(40) not null,
    role_id varchar(40) not null,
    primary key (id)
) engine = InnoDB default charset = utf8;

create table if not exists signup (
	id varchar(40) not null unique,
    `name` varchar(40) not null,
    roles_id varchar(4000) not null,
    primary key (id)
) engine = InnoDB default charset = utf8;

create table if not exists microservice (
	id varchar(40) not null unique,
    `name` varchar(40) not null,
    primary key (id)
) engine = InnoDB default charset = utf8;

create table if not exists url (
	id varchar(40) not null unique,
    ms_id varchar(40) not null,
    `path` varchar(40) not null,
    primary key (id)
) engine = InnoDB default charset = utf8;

create table if not exists `user` (
	username varchar(40) not null unique,
    `password` varchar(40) not null,
    primary key (username)
) engine = InnoDB default charset = utf8;

create table if not exists `role` (
	id varchar(40) not null unique,
    ms_id varchar(40) not null,
    `name` varchar(40) not null,
    primary key (id)
) engine = InnoDB default charset = utf8;

