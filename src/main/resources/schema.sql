CREATE DATABASE IF NOT EXISTS `eureka-tarea1-db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `eureka-tarea1-db`;

-- Table candidates
CREATE TABLE IF NOT EXISTS candidates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    document_type VARCHAR(15) NOT NULL,
    document_number VARCHAR(30) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    place_of_birth VARCHAR(200) NOT NULL,
    date_of_birth DATE NOT NULL,
    address VARCHAR(200) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(50) NOT NULL,
    localization VARCHAR(150) NOT NULL,
    available_start_date DATE NOT NULL,
    available_end_date DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table annexes
CREATE TABLE IF NOT EXISTS annexes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    extension VARCHAR(5) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    candidate_id INT,
    CONSTRAINT fk_candidate FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
