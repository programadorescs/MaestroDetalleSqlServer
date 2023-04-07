# Maestro-Detalle en SQL Server usando controlador JTDS, patrón MVVM y Dagger Hilt

Este proyecto es un ejemplo de una aplicación de maestro-detalle que utiliza SQL Server como base de datos y el controlador JTDS para conectarse a la base de datos. Además, la aplicación utiliza el patrón de arquitectura MVVM y la inyección de dependencia con Dagger Hilt.

## Requisitos

- Android Studio Electric Eel | 2022.1.1 Patch 1 o superior.
- Gradle 7.5 o superior.
- Kotlin 1.8.10 o superior.

## Dependencias

- JTDS: Controlador para conectarse a la base de datos Sql Server.
- Room: Para la implementación de la base de datos.
- ViewModel y LiveData: Para la implementación del patrón MVVM.
- Dagger Hilt: Para la inyección de dependencias.

## Estructura del proyecto

- core: Contiene las clases comunes para la implementación de mensajes, fechas y demas utilidades.
- data: Contiene las clases para la implementación de la base de datos y el repositorio.
- di: Contiene las clases para la configuración de Dagger Hilt.
- ui: Contiene las clases para la implementación de la interfaz de usuario, incluyendo los Fragments y los ViewModels.

## Entidades de nuestra base de datos

![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/ER_Pedido.png)

## Script de la base de datos (dbDatos)

```sql
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
```

## Imagenes de la app

### Menú principal
![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/Screenshot_20230401_021841_pe.pcs.maestrodetallesqlserver.jpg)

### Catálogo de productos para realizar pedidos

![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/Screenshot_20230401_021937_pe.pcs.maestrodetallesqlserver.jpg)

### Indicar la cantidad y/o modificar el precio

![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/Screenshot_20230401_021944_pe.pcs.maestrodetallesqlserver.jpg)

### Lista de productos a confirmar, tiene la opcion de agregar un nombre de cliente
![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/Screenshot_20230401_022002_pe.pcs.maestrodetallesqlserver.jpg)

### Confirmación del pedido
![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/Screenshot_20230401_022014_pe.pcs.maestrodetallesqlserver.jpg)

### Registro o actualizacion de un producto
![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/Screenshot_20230401_022039_pe.pcs.maestrodetallesqlserver.jpg)

### Lista de productos
![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/Screenshot_20230401_022035_pe.pcs.maestrodetallesqlserver.jpg)

### Reporte de pedidos según fechas
![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/Screenshot_20230401_023036_pe.pcs.maestrodetallesqlserver.jpg)

### Muestra el detalle de un pedido realizado
![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/Screenshot_20230401_023104_pe.pcs.maestrodetallesqlserver.jpg)

### Configurar server
Este proceso sera obligatorio para que la app se comunique con la DB, dichos datos (ip, puerto, usuario y clave) serán almacenados de forma encriptada. Los datos que se muestran son solo de ejemplo, ud. deberá de ingresar sus propios datos.
![Image text]()

### Acerca de
![Image text](https://github.com/programadorescs/MaestroDetalleSqlServer/blob/master/app/src/main/assets/Screenshot_20230401_023118_pe.pcs.maestrodetallesqlserver.jpg)

## Conclusiones

Este proyecto es un ejemplo de cómo implementar un maestro-detalle utilizando el controlador jTDS, patrón MVVM e inyección de dependencia con dagger hilt.

