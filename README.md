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

![Image text]()

## Imagenes de la app

### Menú principal
![Image text]()

### Catálogo de productos para realizar pedidos

![Image text]()

### Indicar la cantidad y/o modificar el precio

![Image text]()

### Lista de productos a confirmar, tiene la opcion de agregar un nombre de cliente
![Image text]()

### Confirmación del pedido
![Image text]()

### Registro o actualizacion de un producto
![Image text]()

### Lista de productos
![Image text]()

### Reporte de pedidos según fechas
![Image text]()

### Muestra el detalle de un pedido realizado
![Image text]()

### Anular un pedido según confirmación
![Image text]()

### Ahora el pedido 3 está anulado
![Image text]()

## Conclusiones

Este proyecto es un ejemplo de cómo implementar un maestro-detalle utilizando el controlador jTDS, patrón MVVM e inyección de dependencia con dagger hilt.
