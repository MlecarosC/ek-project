CREATE DATABASE IF NOT EXISTS `eureka-project-db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `eureka-project-db`;

-- Table candidates
CREATE TABLE IF NOT EXISTS candidatos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    tipo_documento VARCHAR(15) NOT NULL,
    numero_documento VARCHAR(30) NOT NULL,
    genero VARCHAR(20) NOT NULL,
    lugar_nacimiento VARCHAR(200) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    codigo_postal VARCHAR(20) NOT NULL,
    pais VARCHAR(50) NOT NULL,
    localizacion VARCHAR(150) NOT NULL,
    disponibilidad_desde DATE NOT NULL,
    disponibilidad_hasta DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table annexes
CREATE TABLE IF NOT EXISTS adjuntos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    extension VARCHAR(5) NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    candidato_id INT,
    CONSTRAINT fk_candidato FOREIGN KEY (candidato_id) REFERENCES candidatos(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
