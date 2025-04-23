## Proyecto de Procesamiento de Facturas - Spring Batch

Este proyecto implementa un proceso por lotes para leer facturas desde una base de datos Oracle, exportarlas a un archivo CSV y actualizar su estado una vez extraidas. Esta desarrollado en **Java 17** usando **Spring Boot 3.4.4**, **Spring Batch**, y **Docker** para facilitar la configuracion del entorno.

## Tecnologias usadas

- Java 17
- Spring Boot 3.4.4
- Spring Batch
- Oracle Database
- Docker + Docker Compose
- JUnit 5 + Mockito
- Lombok

---

## Arquitectura

El proyecto sigue una estructura modular con principios de codigo limpio y separacion de responsabilidades:

- **Reader:** Carga facturas desde Oracle usando un `ItemReader`.
- **Writer:** Exporta facturas a un archivo CSV.
- **PostWriter:** Actualiza el estado de las facturas en la base de datos.

---

## Configuracion del entorno

### Requisitos

- Docker
- Java 17
- Maven 3.8+

### Levantar Oracle con Docker

```bash
docker-compose up -d
```

> Asegurate de tener configurada la imagen de Oracle y los scripts de inicializacion si son necesarios.

---

## Compilacion y ejecucion

### Construir el proyecto

```bash
mvn clean install
```

### Ejecutar el Job

```bash
java -jar target/facturas-0.0.1-SNAPSHOT.jar fecha=2025-06-30 outputFile="D:\ruta\facturas_output.csv"
```

> Puedes pasar la fecha de corte y el path de salida como parametros del job.

---

## Estructura del CSV generado

El archivo contiene el siguiente encabezado:

```
CODIGO_FACTURA,CODIGO_PROVEEDOR,IMPORTE,DIVISA,FECHA_VENCIMIENTO,IBAN
```

Los datos se exportan en formato delimitado por comas (CSV).

---

## Pruebas

Este proyecto incluye pruebas unitarias para verificar la configuracion del `ItemWriter` y los componentes del flujo batch.

```bash
mvn test
```
