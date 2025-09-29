# Eureka Tarea 1 - API REST

API REST desarrollada con Spring Boot 3.5.6 para la gestión de candidatos y sus documentos correspondientes al Programa Eureka 2025 - Semana 1.

## Descripción

Este proyecto implementa una API REST que maneja dos entidades principales:
- **Candidato** (Entidad Principal): Gestiona la información de candidatos
- **Documento** (Entidad Secundaria): Gestiona los archivos/documentos asociados a cada candidato

La relación entre ambas entidades es de **uno a muchos** (un candidato puede tener múltiples documentos).

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

#### Obtener documentos de un candidato
```http
GET /api/v1/candidatos/{id}/documentos
```

### Gestión de Documentos (Entidad Secundaria)

Los docuemntos se gestionan a través del endpoint de candidatos, manteniendo la relación entre ambas entidades.

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

### 4. Obtener documentos de un candidato

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

- **nomobre**: Requerido, máximo 50 caracteres
- **email**: Requerido, formato de email válido, máximo 150 caracteres
- **fechaNacimiento**: No puede ser fecha futura
- **disponibilidadDesde/disponibilidadHasta**: Requeridas
- Y más validaciones según los requisitos del negocio

## Manejo de Errores

### Error 404 - No encontrado
```json
{
    "timestamp": "2025-01-15",
    "code": 404,
    "message": "No se encontró un candidato con el ID dado 999",
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
