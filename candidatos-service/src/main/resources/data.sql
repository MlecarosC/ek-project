USE `eureka-project-db`;

-- Insert candidatos
INSERT IGNORE INTO candidatos (nombre, apellidos, email, telefono, tipo_documento, numero_documento, genero, lugar_nacimiento, fecha_nacimiento, direccion, codigo_postal, pais, localizacion, disponibilidad_desde, disponibilidad_hasta)
VALUES
('Juan', 'Perez', 'juan.perez@email.com', '123456789', 'DNI', '12345678', 'M', 'Buenos Aires', '1990-01-01', 'Calle Falsa 123', '1000', 'Argentina', 'Buenos Aires', '2025-01-01', '2025-12-31'),
('Maria', 'Lopez', 'maria.lopez@email.com', '987654321', 'DNI', '87654321', 'F', 'Cordoba', '1985-05-15', 'Avenida Siempre Viva 742', '5000', 'Argentina', 'Cordoba', '2025-02-01', '2025-11-30'),
('Carlos', 'Garcia', 'carlos.garcia@email.com', '555123456', 'Passport', 'AA123456', 'M', 'Rosario', '1992-03-22', 'Boulevard Central 456', '2000', 'Argentina', 'Rosario', '2025-03-01', '2025-10-31'),
('Lucia', 'Martinez', 'lucia.martinez@email.com', '444987654', 'DNI', '99887766', 'F', 'Mendoza', '1995-07-10', 'Calle Nueva 789', '5500', 'Argentina', 'Mendoza', '2025-04-01', '2025-09-30');

-- Insert documentos
INSERT IGNORE INTO adjuntos (extension, nombre_archivo, candidato_id)
VALUES
-- Juan Perez
('pdf', 'cv_juan_perez.pdf', (SELECT id FROM candidatos WHERE email = 'juan.perez@email.com')),
('jpg', 'photo_juan_perez.jpg', (SELECT id FROM candidatos WHERE email = 'juan.perez@email.com')),

-- Maria Lopez
('pdf', 'cv_maria_lopez.pdf', (SELECT id FROM candidatos WHERE email = 'maria.lopez@email.com')),

-- Carlos Garcia
('pdf', 'cv_carlos_garcia.pdf', (SELECT id FROM candidatos WHERE email = 'carlos.garcia@email.com')),
('png', 'photo_carlos_garcia.png', (SELECT id FROM candidatos WHERE email = 'carlos.garcia@email.com')),

-- Lucia Martinez
('pdf', 'cv_lucia_martinez.pdf', (SELECT id FROM candidatos WHERE email = 'lucia.martinez@email.com')),
('jpg', 'photo_lucia_martinez.jpg', (SELECT id FROM candidatos WHERE email = 'lucia.martinez@email.com')),
('png', 'extra_lucia_martinez.png', (SELECT id FROM candidatos WHERE email = 'lucia.martinez@email.com'));
