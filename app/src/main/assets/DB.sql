use master;
go

Create database dbDatos;
go
use dbDatos;
go

Create table producto(
id int primary key identity(1,1) not null,
descripcion nvarchar(200) not null unique,
precio decimal(13,2) not null
);
go

Create table pedido(
id int primary key identity(1,1) not null,
fecha datetime not null,
cliente nvarchar(200) not null default 'Publico general',
estado nvarchar(20) not null default 'Vigente',
total decimal(13,2) not null
);
go

Create table detallePedido(
id int primary key identity(1,1) not null,
idpedido int not null References Pedido(id),
idproducto int not null References Producto(id),
cantidad int not null,
precio decimal(13,2) not null,
importe decimal(13,2) not null
);
go

Insert Into Producto(descripcion, precio) Values('Gaseosa Pepsi 1/2 Lt', 1.5);
Insert Into Producto(descripcion, precio) Values('Galleta soda grande', 1.8);