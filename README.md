## Requisitos

Antes de ejecutar el programa, asegúrate de tener instalado:

- Java (versión 8 o superior)
- MariaDB o MySQL.

El programa recibe dos argumentos para poder funcionar. El primero será un archivo de configuracion en formato JSON y el
segundo el archivo CSV a importar y procesar.

## Configuración

Crea un archivo de configuración en formato JSON con la siguiente estructura:

```json
{
  "databaseConfig": {
    "url": "jdbc:mariadb://localhost:3306",
    "databaseName": "my_database",
    "username": "my_user",
    "password": "my_password"
  },
  "csvPathConfig": {
    "path": "/path/to/exported/csv/directory"
  }
}
```

* databaseConfig: Contiene la configuración de la base de datos.
* url: URL de conexión a la base de datos MariaDB.
* username: Nombre de usuario para acceder a la base de datos.
* password: Contraseña del usuario para acceder a la base de datos.
* databaseName: Nombre de la base de datos que se creará para almacenar los datos.
* csvPathConfig: Contiene la configuración de la ruta del archivo CSV.
* path: Ruta absoluta o relativa del archivo CSV que se importará en la base de datos.

## Estructura del archivo CSV

El archivo csv debe contar con los siguientes encabezados para poder ser leido, importado y procesado. 

```
    Order ID
    Order Priority
    Order Date
    Region
    Country
    Item Type
    Sales Channel
    Ship Date
    Units Sold
    Unit Price
    Unit Cost
    Total Revenue
    Total Cost
    Total Profit
```

## Tras la ejecución

Una vez que el programa haya finalizado la importación de datos y generado el resumen, podrás encontrar un archivo CSV con el resumen en el directorio especificado en la configuración. Con esto, terminará la ejecución del programa.

