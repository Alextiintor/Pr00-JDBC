-- drop database if exists tienda;

create database tienda with encoding "UTF8" lc_collate = "Spanish_Spain" lc_ctype = "Spanish_Spain";

drop table if exists productos_pack;
drop table if exists pack;
drop table if exists producto;

drop table if exists telefon_persona;
drop table if exists cliente;
drop table if exists proveedor;
drop table if exists persona;
drop type if exists direccion;

drop table if exists asistencia;


create table producto (
	id SERIAL primary key,
	nombre VARCHAR(15) not null,
	precio real not null,
	stock integer not null,
	fecha_inicio date not null,
	fecha_fin date not null,

	constraint chk_precio CHECK(precio>0),
	constraint chk_stock CHECK(stock>0),
	constraint chk_catalogo CHECK(fecha_fin>=fecha_inicio)
);

create table pack(
	dto real not null,
	
    unique (id),
	constraint chk_dto CHECK(dto>=0 and dto<=100)
)inherits(producto);

create table productos_pack(
	id_pack INTEGER,
	id_producto INTEGER,

	primary key(id_pack, id_producto),
	CONSTRAINT fk_id_pack FOREIGN key(id_pack) references pack(id),
	CONSTRAINT fk_id_producto FOREIGN key(id_producto) references producto(id)
);

create type direccion AS(
	localidad VARCHAR(25),
	provincia VARCHAR(20),
	cp VARCHAR(5),
	calle VARCHAR(20)
);

create table persona(
	id SERIAL primary key,
	dni VARCHAR(10) not null,
	name VARCHAR(15) not null,
	lastname VARCHAR(20) not null,
	fecha_nacimiento DATE not null,
	email VARCHAR(25) not null,
	telefonos varchar(15) array,
	direccion direccion not null
);

create table cliente(
)inherits (persona);

create table proveedor(
)inherits (persona);

create table asistencia(
	id SERIAL primary key,
	fecha_entrada timestamp,
	fecha_salida timestamp
);