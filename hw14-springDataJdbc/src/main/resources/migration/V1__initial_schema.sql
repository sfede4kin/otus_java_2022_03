create table client
(
    id   bigserial not null primary key,
    name varchar(50) not null
);

create table address(
    client_id bigint not null references client (id),
	street    varchar(255) not null
);

create table phone(
    id        bigserial not null primary key,
	number    varchar(255) not null,
	client_id bigint not null references client (id)
);
