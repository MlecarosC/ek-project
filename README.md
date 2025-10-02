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
- **Docker & Docker Compose**

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

- **Docker Desktop**
- **Git**

## Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/MlecarosC/ek-tarea1.git
cd ek-tarea1
```

### 2. Levantar la Aplicación con Docker

> **ℹ️ Nota**: Docker se encarga de crear automáticamente la base de datos, las tablas y cargar los datos de prueba. No necesitas configurar nada manualmente.

**Iniciar la aplicación:**

```bash
# Primera vez (construir y ejecutar)
docker-compose up --build

# Ejecuciones posteriores
docker-compose up

# Ejecutar en segundo plano
docker-compose up -d
```

La aplicación se ejecutará en `http://localhost:8080`

### 3. Detener la Aplicación

```bash
# Detener contenedores
docker-compose stop

# Detener y eliminar contenedores
docker-compose down

# Detener y eliminar contenedores y datos de BD (reset completo)
docker-compose down -v
```

### 4. Comandos Útiles

```bash
# Ver logs en tiempo real
docker-compose logs -f

# Ver logs solo de la API
docker-compose logs -f api

# Ver logs solo de MySQL
docker-compose logs -f mysql

# Ver estado de los contenedores
docker-compose ps

# Reconstruir después de cambios en el código
docker-compose up --build -d

# Acceder a la base de datos
docker exec -it eureka-mysql mysql -u eureka_user -p
# Contraseña: eureka_pass
```

## ⚠️ Importante: Comportamiento de Datos de Prueba

La aplicación incluye **datos de prueba** que se cargan automáticamente cada vez que inicias la aplicación:

- 4 candidatos de ejemplo con sus respectivos anexos
- Los datos se insertan usando `INSERT IGNORE`, por lo que no se duplican

**📝 Nota sobre eliminación de registros:**
Si eliminas candidatos durante las pruebas y reinicias la aplicación, los candidatos eliminados se recrearán automáticamente, pero con **IDs superiores** (esto es el comportamiento normal de MySQL con AUTO_INCREMENT).

**🛠️ Para evitar la carga automática de datos de prueba:**

Edita el archivo `docker-compose.yml` en la sección de la API y cambia:
```yaml
environment:
  SPRING_SQL_INIT_MODE: never  # Cambiar de 'always' a 'never'
```

Luego reinicia los contenedores:
```bash
docker-compose down
docker-compose up -d
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

Los documentos se gestionan a través del endpoint de candidatos, manteniendo la relación entre ambas entidades.

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

- **nombre**: Requerido, máximo 50 caracteres
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

## Solución de Problemas

### Puerto 8080 ya en uso

**Windows:**
```bash
netstat -ano | findstr :8080
```

**Mac/Linux:**
```bash
lsof -i :8080
```

**Solución:** Cambia el puerto en `docker-compose.yml`:
```yaml
ports:
  - "8081:8080"  # Usar puerto 8081 en lugar de 8080
```

### MySQL no inicia correctamente

```bash
# Ver logs detallados
docker-compose logs mysql

# Eliminar volumen y reintentar
docker-compose down -v
docker-compose up --build
```

### La API no se conecta a MySQL

```bash
# Verificar que MySQL esté saludable
docker-compose ps

# Ver logs de la API
docker-compose logs api

# Verificar conectividad desde la API a MySQL
docker exec -it eureka-api ping mysql
```

### Cambios en el código no se reflejan

```bash
# Reconstruir la imagen sin caché
docker-compose down
docker-compose build --no-cache api
docker-compose up
```

## Autor

- **Desarrollador**: Martin Lecaros
- **Programa**: Eureka 2025 - Desarrollador Full Stack
