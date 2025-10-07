# Eureka Project - Microservicios con Spring Cloud

Sistema de microservicios desarrollado con Spring Boot 3.5.6 para la gestión de candidatos y sus documentos correspondientes, implementando arquitectura de microservicios con Eureka Server, API Gateway y servicios independientes.

## Descripción

Este proyecto implementa una arquitectura de microservicios completa que incluye:

### **Arquitectura de Microservicios**
- **Eureka Server** (Puerto 8761): Servidor de registro y descubrimiento de servicios
- **API Gateway** (Puerto 8090): Punto de entrada único para todos los servicios
- **Candidato Service** (Puerto 8080): Microservicio de gestión de candidatos
- **Adjunto Service** (Puerto 8081): Microservicio de gestión de documentos adjuntos
- **MySQL Database** (Puerto 3307): Base de datos compartida

### **Entidades**
- **Candidato** (Entidad Principal): Gestiona la información de candidatos
- **Adjunto** (Entidad Secundaria): Gestiona los archivos/documentos asociados a cada candidato

## Tecnologías Utilizadas

### Stack Principal
- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Cloud 2025.0.0**
  - Spring Cloud Netflix Eureka (Server & Client)
  - Spring Cloud Gateway
- **Spring Data JPA**
- **Spring Boot Validation**
- **Spring Boot Actuator**
- **MySQL 8.x**
- **Lombok**
- **ModelMapper 3.2.4**
- **Maven**

### Infraestructura
- **Docker & Docker Compose**

### Testing
- **JUnit 5**
- **Testcontainers** (MySQL)
- **RestAssured**
- **Hamcrest**

## Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                      Cliente HTTP                            │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │  API Gateway    │
                  │   (Port 8090)   │
                  └────────┬────────┘
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
┌───────────────┐  ┌──────────────┐  ┌──────────────┐
│ Eureka Server │  │  Candidato   │  │   Adjunto    │
│  (Port 8761)  │  │   Service    │  │   Service    │
└───────────────┘  │  (Port 8080) │  │  (Port 8081) │
                   └──────┬───────┘  └──────┬───────┘
                          │                 │
                          └────────┬────────┘
                                   ▼
                          ┌─────────────────┐
                          │  MySQL Database │
                          │   (Port 3307)   │
                          └─────────────────┘
```

## Estructura del Proyecto

```
ek-tarea1/
├── eureka-server/              # Servidor de descubrimiento
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── gateway-server/             # API Gateway
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── candidatos-service/         # Microservicio de candidatos
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/eureka/api/
│   │   │   │   ├── configuration/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── exception/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   └── service/
│   │   │   └── resources/
│   │   │       ├── schema.sql
│   │   │       ├── data.sql
│   │   │       └── application.properties
│   │   └── test/
│   │       ├── java/com/eureka/api/
│   │       │   ├── config/
│   │       │   ├── fixtures/
│   │       │   └── integration/
│   │       └── resources/
│   │           └── application-test.properties
│   ├── Dockerfile
│   └── pom.xml
├── adjuntos-service/           # Microservicio de adjuntos
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml          # Orquestación de servicios
└── README.md
```

## Requisitos Previos

- **Docker Desktop** (con Docker Compose)
- **Git**

## Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/MlecarosC/ek-tarea1.git
cd ek-tarea1
```

### 2. Levantar la Aplicación con Docker Compose

**Iniciar todos los servicios:**

```bash
# Primera vez (construir y ejecutar)
docker-compose up --build

# Ejecuciones posteriores
docker-compose up

# Ejecutar en segundo plano
docker-compose up -d
```

### 3. Verificar que los Servicios están Activos

```bash
# Ver estado de todos los contenedores
docker-compose ps

# Deberías ver todos los servicios como "Up" y "healthy"
```

**Acceso a los servicios:**
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8090
- **Candidato Service (directo)**: http://localhost:8080
- **Adjunto Service (directo)**: http://localhost:8081
- **MySQL**: localhost:3307

### 4. Detener la Aplicación

```bash
# Detener contenedores (mantiene datos)
docker-compose stop

# Detener y eliminar contenedores (mantiene datos en volumen)
docker-compose down

# Detener, eliminar contenedores Y eliminar datos de BD (reset completo)
docker-compose down -v
```

## Endpoints de la API

### A través del API Gateway

Todos los servicios están disponibles a través del API Gateway en el puerto **8090**:

#### Gestión de Candidatos
```http
# Crear un candidato
POST http://localhost:8090/api/v1/candidatos

# Obtener todos los candidatos
GET http://localhost:8090/api/v1/candidatos

# Obtener candidato por ID
GET http://localhost:8090/api/v1/candidatos/{id}

# Eliminar candidato por ID
DELETE http://localhost:8090/api/v1/candidatos/{id}
```

#### Gestión de Documentos
```http
# Obtener todos los adjuntos
GET http://localhost:8090/api/v1/adjuntos

# Obtener adjuntos por ID de candidato
GET http://localhost:8090/api/v1/adjuntos/candidato/{id}
```

### Acceso Directo a los Servicios

**Candidato Service** (Puerto 8080):
```http
GET http://localhost:8080/api/v1/candidatos
POST http://localhost:8080/api/v1/candidatos
GET http://localhost:8080/api/v1/candidatos/{id}
DELETE http://localhost:8080/api/v1/candidatos/{id}
```

**Adjunto Service** (Puerto 8081):
```http
GET http://localhost:8081/api/v1/adjuntos
GET http://localhost:8081/api/v1/adjuntos/candidato/{id}
```

## Ejemplos de Uso

### 1. Crear un nuevo candidato (vía Gateway)

**Request:**
```bash
curl -X POST http://localhost:8090/api/v1/candidatos \
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
  "id": 5,
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
}
```

### 2. Obtener todos los candidatos (vía Gateway)

**Request:**
```bash
curl -X GET http://localhost:8090/api/v1/candidatos
```

### 3. Obtener documentos de un candidato (vía Gateway)

**Request:**
```bash
curl -X GET http://localhost:8090/api/v1/adjuntos/candidato/1
```

**Response:**
```json
[
  {
    "candidatoId": 1,
    "extension": "pdf",
    "nombreArchivo": "cv_juan_perez.pdf"
  },
  {
    "candidatoId": 1,
    "extension": "jpg",
    "nombreArchivo": "photo_juan_perez.jpg"
  }
]
```

### 4. Eliminar un candidato (vía Gateway)

**Request:**
```bash
curl -X DELETE http://localhost:8090/api/v1/candidatos/1
```

## Manejo de Errores

### Error 404 - No encontrado
```json
{
  "timestamp": "2025-10-06",
  "code": 404,
  "message": "No se encontró un candidato con el ID dado 999",
  "path": "/api/v1/candidatos/999"
}
```

### Error 400 - Validación
```json
{
  "timestamp": "2025-10-06",
  "code": 400,
  "message": "Validation failed",
  "path": "/api/v1/candidatos",
  "validationErrors": {
    "nombre": "El nombre es obligatorio",
    "email": "El correo electrónico debe ser válido"
  }
}
```

### Error 409 - Conflicto (email duplicado)
```json
{
  "timestamp": "2025-10-06",
  "code": 409,
  "message": "Email existente",
  "path": "/api/v1/candidatos"
}
```

## Testing con Testcontainers

El proyecto incluye tests de integración automatizados utilizando **Testcontainers** para levantar contenedores de MySQL durante la ejecución de los tests.

### Estructura de Tests

Cada microservicio tiene su propia carpeta tests:

```
candidatos-service/src/test/
├── java/com/eureka/api/
│   ├── config/
│   │   └── BaseConfig.java           # Configuración base para tests
│   ├── fixtures/
│   │   └── CandidateFixture.java     # Factory de datos de prueba
│   └── integration/
│       └── CandidatoControllerTest.java  # Tests de integración
└── resources/
    └── application-test.properties    # Configuración para tests

adjuntos-service/src/test/
├── java/com/eureka/api/
│   ├── config/
│   │   └── BaseConfig.java
│   ├── fixtures/
│   │   └── AdjuntoFixture.java
│   └── integration/
│       └── AdjuntoControllerTest.java
└── resources/
    └── application-test.properties
```

### Ejecutar Tests

**Con Maven Wrapper**

```bash
# Ejecutar tests del servicio de candidatos
cd candidatos-service
./mvnw test

# Ejecutar tests del servicio de adjuntos
cd adjuntos-service
./mvnw test

# Ejecutar tests con más detalle
./mvnw test -X
```

### Características de los Tests

**Testcontainers automáticamente:**
- Descarga la imagen de MySQL 8.0
- Levanta un contenedor MySQL antes de cada test
- Crea las tablas usando Hibernate (ddl-auto=create-drop)
- Limpia la base de datos antes de cada test
- Destruye el contenedor al finalizar

**Cobertura de Tests:**

**Candidato Service:**
- Crear candidato con datos válidos
- Validación de campos obligatorios
- Email duplicado (409 Conflict)
- Obtener todos los candidatos
- Obtener candidato por ID
- Candidato no encontrado (404)
- Eliminar candidato
- Eliminar candidato inexistente (404)

**Adjunto Service:**
- Obtener todos los adjuntos
- Obtener adjuntos por ID de candidato
- Adjuntos no encontrados (404)
- Verificar estructura de respuesta
- Aislamiento de documentos por candidato

### Ejemplo de Salida de Tests

```bash
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 12.34 s
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

## Validaciones

La API incluye validaciones automáticas para todos los campos:

**Candidato:**
- **nombre**: Requerido, máximo 50 caracteres
- **apellidos**: Requerido, máximo 50 caracteres
- **email**: Requerido, formato de email válido, único, máximo 150 caracteres
- **telefono**: Requerido, máximo 20 caracteres
- **tipoDocumento**: Requerido, máximo 15 caracteres
- **numeroDocumento**: Requerido, máximo 30 caracteres
- **genero**: Requerido, máximo 20 caracteres
- **lugarNacimiento**: Requerido, máximo 200 caracteres
- **fechaNacimiento**: Requerido, no puede ser fecha futura
- **direccion**: Requerido, máximo 200 caracteres
- **codigoPostal**: Requerido, máximo 20 caracteres
- **pais**: Requerido, máximo 50 caracteres
- **localizacion**: Requerido, máximo 150 caracteres
- **disponibilidadDesde**: Requerido
- **disponibilidadHasta**: Requerido

## Autor

**Desarrollador**: Martin Lecaros  
**Programa**: Eureka 2025 - Desarrollador Full Stack  
