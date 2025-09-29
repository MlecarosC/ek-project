# Eureka Tarea 1 - API REST

<details>
<summary><strong>🇪🇸 Español</strong></summary>

API REST desarrollada con Spring Boot 3.5.6 para la gestión de candidatos y sus anexos correspondientes al Programa Eureka 2025 - Semana 1.

## Descripción

Este proyecto implementa una API REST que maneja dos entidades principales:
- **Candidate** (Entidad Principal): Gestiona la información de candidatos
- **Annex** (Entidad Secundaria): Gestiona los archivos/anexos asociados a cada candidato

La relación entre ambas entidades es de **uno a muchos** (un candidato puede tener múltiples anexos).

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **Spring Boot Validation**
- **MySQL 8.x**
- **Lombok**
- **ModelMapper 3.2.4**
- **Maven**

## Estructura del Proyecto

```
src/main/java/com/eureka/tarea1_api/
├── configuration/          # Configuraciones (ModelMapper)
├── controller/            # Controladores REST
├── dto/                  # Data Transfer Objects
├── exception/            # Manejo de excepciones
├── model/               # Entidades JPA
├── repository/          # Repositorios JPA
├── service/            # Lógica de negocio
└── Tarea1ApiApplication.java
```

## Requisitos Previos

- **Java 17 o 21**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

## Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/MlecarosC/ek-tarea1.git
cd ek-tarea1
```

### 2. Configurar Base de Datos

> **ℹ️ Nota**: El proyecto incluye scripts SQL que crean automáticamente la base de datos si no existe, por lo que no es necesario crearla manualmente.

1. **Configurar credenciales de base de datos:**

El proyecto no incluye el archivo `application.properties` por razones de seguridad. En su lugar, proporciona un archivo de ejemplo que debes usar como plantilla:

```bash
# Copia el archivo de ejemplo y crea tu configuración local
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

2. **Editar el archivo `application.properties` con tus credenciales:**

```properties
spring.application.name=Tarea1-api

# Configuración de base de datos - La DB se crea automáticamente
spring.datasource.url=jdbc:mysql://localhost:3306/eureka-tarea1-db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=TU_USUARIO_DB
spring.datasource.password=TU_CONTRASEÑA_DB

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Scripts SQL - Se ejecutan automáticamente al iniciar
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql
```

> **⚠️ Importante**: Asegúrate de tener MySQL funcionando en tu sistema y reemplaza `TU_USUARIO_DB` y `TU_CONTRASEÑA_DB` con tus credenciales reales.

### 3. Compilar el Proyecto

```bash
./mvnw clean compile
```

### 4. Ejecutar la Aplicación

```bash
./mvnw spring-boot:run
```

La aplicación se ejecutará en `http://localhost:8080`

## ⚠️ Importante: Comportamiento de Datos de Prueba

La aplicación incluye **datos de prueba** que se cargan automáticamente cada vez que inicias la aplicación:

- 4 candidatos de ejemplo con sus respectivos anexos
- Los datos se insertan usando `INSERT IGNORE`, por lo que no se duplican

**📝 Nota sobre eliminación de registros:**
Si eliminas candidatos durante las pruebas y reinicias la aplicación, los candidatos eliminados se recrearán automáticamente, pero con **IDs superiores** (esto es el comportamiento normal de MySQL con AUTO_INCREMENT).

**🛠️ Para evitar la carga automática de datos de prueba:**

Edita tu archivo `application.properties` y cambia:
```properties
# Desactivar carga automática de datos
spring.sql.init.mode=never
```

O comenta las líneas:
```properties
# spring.sql.init.data-locations=classpath:data.sql
```

## Endpoints de la API

### Gestión de Candidatos (Entidad Principal)

#### Crear un candidato
```http
POST /api/v1/candidatos
Content-Type: application/json
```

#### Obtener todos los candidatos
```http
GET /api/v1/candidatos
```

#### Obtener candidato por ID
```http
GET /api/v1/candidatos/{id}
```

#### Eliminar candidato por ID
```http
DELETE /api/v1/candidatos/{id}
```

#### Obtener anexos de un candidato
```http
GET /api/v1/candidatos/{id}/annexes
```

### Gestión de Anexos (Entidad Secundaria)

Los anexos se gestionan a través del endpoint de candidatos, manteniendo la relación entre ambas entidades.

## Ejemplos de Uso

### 1. Crear un nuevo candidato

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/candidatos \
  -H "Content-Type: application/json" \
  -d '{
        "nombre": "Ana",
        "apellidos": "González",
        "email": "ana.gonzalez@example.com",
        "telefono": "+56912345678",
        "tipoDocumento": "RUT",
        "numeroDocumento": "12.345.678-9",
        "genero": "F",
        "lugarNacimiento": "Valparaíso, Chile",
        "fechaNacimiento": "1985-03-15",
        "direccion": "Avenida Brasil 456",
        "codigoPostal": "2340000",
        "pais": "Chile",
        "localizacion": "Valparaíso, Chile",
        "disponibilidadDesde": "2025-02-01",
        "disponibilidadHasta": "2025-11-30"
    }'
```

**Response:**
```json
{
    "id": 1,
    "nombre": "Juan",
    "apellidos": "Perez",
    "email": "juan.perez@email.com",
    "telefono": "123456789",
    "tipoDocumento": "DNI",
    "numeroDocumento": "12345678",
    "genero": "M",
    "lugarNacimiento": "Buenos Aires",
    "fechaNacimiento": "1990-01-01",
    "direccion": "Calle Falsa 123",
    "codigoPostal": "1000",
    "pais": "Argentina",
    "localizacion": "Buenos Aires",
    "disponibilidadDesde": "2025-01-01",
    "disponibilidadHasta": "2025-12-31"
}
```

### 2. Obtener todos los candidatos

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/candidatos
```

### 3. Obtener candidato por ID

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/candidatos/1
```

### 4. Obtener anexos de un candidato

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/candidatos/1/documentos
```

**Response:**
```json
[
    {
        "candidato": 1,
        "extension": "pdf",
        "nombreArchivo": "cv_juan_perez.pdf"
    },
    {
        "candidato": 1,
        "extension": "jpg",
        "nombreArchivo": "photo_juan_perez.jpg"
    }
]
```

### 5. Eliminar un candidato

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/v1/candidatos/1
```

## Validaciones

La API incluye validaciones automáticas para todos los campos:

- **name**: Requerido, máximo 50 caracteres
- **email**: Requerido, formato de email válido, máximo 150 caracteres
- **dateOfBirth**: No puede ser fecha futura
- **availableStartDate/availableEndDate**: Requeridas
- Y más validaciones según los requisitos del negocio

## Manejo de Errores

### Error 404 - No encontrado
```json
{
    "timestamp": "2025-01-15",
    "code": 404,
    "message": "No candidate with the given ID 999",
    "path": "/api/v1/candidatos/999"
}
```

### Error 400 - Validación
```json
{
    "timestamp": "2025-01-15",
    "code": 400,
    "message": "Validation failed",
    "path": "/api/v1/candidatos",
    "validationErrors": {
        "nombre": "El nombre es obligatorio",
        "email": "El correo electrónico debe ser válido"
    }
}
```

### Error 409 - Conflicto email duplicado
```json
{
    "timestamp": "2025-09-26",
    "code": 409,
    "message": "Email existente",
    "path": "/api/v1/candidatos"
}
```

## Autor

- **Desarrollador**: Martin Lecaros
- **Programa**: Eureka 2025 - Desarrollador Full Stack

</details>

---

<details>
<summary><strong>🇺🇸 English</strong></summary>

# Eureka Task 1 - REST API

REST API developed with Spring Boot 3.5.6 for managing candidates and their annexes for the Eureka 2025 Program - Week 1.

## Description

This project implements a REST API that handles two main entities:
- **Candidate** (Primary Entity): Manages candidate information
- **Annex** (Secondary Entity): Manages files/annexes associated with each candidate

The relationship between both entities is **one-to-many** (one candidate can have multiple annexes).

## Technologies Used

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **Spring Boot Validation**
- **MySQL 8.x**
- **Lombok**
- **ModelMapper 3.2.4**
- **Maven**

## Project Structure

```
src/main/java/com/eureka/tarea1_api/
├── configuration/          # Configurations (ModelMapper)
├── controller/            # REST Controllers
├── dto/                  # Data Transfer Objects
├── exception/            # Exception handling
├── model/               # JPA Entities
├── repository/          # JPA Repositories
├── service/            # Business logic
└── Tarea1ApiApplication.java
```

## Prerequisites

- **Java 17 or 21**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/MlecarosC/ek-tarea1.git
cd ek-tarea1
```

### 2. Configure Database

> **ℹ️ Note**: The project includes SQL scripts that automatically create the database if it doesn't exist, so manual database creation is not necessary.

1. **Configure database credentials:**

The project doesn't include the `application.properties` file for security reasons. Instead, it provides an example file that you should use as a template:

```bash
# Copy the example file and create your local configuration
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

2. **Edit the `application.properties` file with your credentials:**

```properties
spring.application.name=Tarea1-api

# Database configuration - DB is created automatically
spring.datasource.url=jdbc:mysql://localhost:3306/eureka-tarea1-db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# SQL Scripts - Run automatically on startup
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:schema.sql
spring.sql.init.data-locations=classpath:data.sql
```

> **⚠️ Important**: Make sure you have MySQL running on your system and replace `YOUR_DB_USERNAME` and `YOUR_DB_PASSWORD` with your actual credentials.

### 3. Compile the Project

```bash
./mvnw clean compile
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The application will run on `http://localhost:8080`

## ⚠️ Important: Test Data Behavior

The application includes **test data** that loads automatically every time you start the application:

- 4 example candidates with their respective annexes
- Data is inserted using `INSERT IGNORE`, so it won't duplicate

**📝 Note about record deletion:**
If you delete candidates during testing and restart the application, the deleted candidates will be recreated automatically, but with **higher IDs** (this is normal MySQL behavior with AUTO_INCREMENT).

**🛠️ To prevent automatic test data loading:**

Edit your `application.properties` file and change:
```properties
# Disable automatic data loading
spring.sql.init.mode=never
```

Or comment out the lines:
```properties
# spring.sql.init.data-locations=classpath:data.sql
```

## API Endpoints

### Candidate Management (Primary Entity)

#### Create a candidate
```http
POST /api/v1/candidatos
Content-Type: application/json
```

#### Get all candidates
```http
GET /api/v1/candidatos
```

#### Get candidate by ID
```http
GET /api/v1/candidatos/{id}
```

#### Delete candidate by ID
```http
DELETE /api/v1/candidatos/{id}
```

#### Get candidate's annexes
```http
GET /api/v1/candidatos/{id}/documentos
```

### Annex Management (Secondary Entity)

Annexes are managed through the candidate endpoints, maintaining the relationship between both entities.

## Usage Examples

### 1. Create a new candidate

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/candidatos \
  -H "Content-Type: application/json" \
  -d '{
        "nombre": "Ana",
        "apellidos": "González",
        "email": "ana.gonzalez@example.com",
        "telefono": "+56912345678",
        "tipoDocumento": "RUT",
        "numeroDocumento": "12.345.678-9",
        "genero": "F",
        "lugarNacimiento": "Valparaíso, Chile",
        "fechaNacimiento": "1985-03-15",
        "direccion": "Avenida Brasil 456",
        "codigoPostal": "2340000",
        "pais": "Chile",
        "localizacion": "Valparaíso, Chile",
        "disponibilidadDesde": "2025-02-01",
        "disponibilidadHasta": "2025-11-30"
    }'
```

**Response:**
```json
{
    "id": 1,
    "nombre": "Juan",
    "apellidos": "Perez",
    "email": "juan.perez@email.com",
    "telefono": "123456789",
    "tipoDocumento": "DNI",
    "numeroDocumento": "12345678",
    "genero": "M",
    "lugarNacimiento": "Buenos Aires",
    "fechaNacimiento": "1990-01-01",
    "direccion": "Calle Falsa 123",
    "codigoPostal": "1000",
    "pais": "Argentina",
    "localizacion": "Buenos Aires",
    "disponibilidadDesde": "2025-01-01",
    "disponibilidadHasta": "2025-12-31"
}
```

### 2. Get all candidates

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/candidatos
```

### 3. Get candidate by ID

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/candidatos/1
```

### 4. Get candidate's annexes

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/candidatos/1/documentos
```

**Response:**
```json
[
    {
        "candidatoId": 3,
        "extension": "pdf",
        "nombreArchivo": "cv_carlos_garcia.pdf"
    },
    {
        "candidatoId": 3,
        "extension": "png",
        "nombreArchivo": "photo_carlos_garcia.png"
    }
]
```

### 5. Delete a candidate

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/v1/candidatos/1
```

## Validations

The API includes automatic validations for all fields:

- **name**: Required, maximum 50 characters
- **email**: Required, valid email format, maximum 150 characters
- **dateOfBirth**: Cannot be a future date
- **availableStartDate/availableEndDate**: Required
- And more validations according to business requirements

## Error Handling

### Error 404 - Not Found
```json
{
    "timestamp": "2025-01-15",
    "code": 404,
    "message": "No se encontró un candidato con el ID dado 999",
    "path": "/api/v1/candidatos/999"
}
```

### Error 400 - Validation
```json
{
    "timestamp": "2025-01-15",
    "code": 400,
    "message": "Validation failed",
    "path": "/api/v1/candidatos",
    "validationErrors": {
        "name": "El nombre es obligatorio",
        "email": "Email must be valid"
    }
}
```

### Error 409 - Duplicated Email
```json
{
    "timestamp": "2025-09-26",
    "code": 409,
    "message": "Email existente",
    "path": "/api/v1/candidatos"
}
```

## Author

- **Developer**: Martin Lecaros
- **Program**: Eureka 2025 - Full Stack Developer

</details>