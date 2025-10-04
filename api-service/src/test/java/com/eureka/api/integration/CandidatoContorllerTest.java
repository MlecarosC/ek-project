package com.eureka.api.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.eureka.api.dto.CandidateDTO;
import com.eureka.api.fixtures.CandidateFixture;
import com.eureka.api.model.Candidato;
import com.eureka.api.repository.CandidatoRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CandidatoContorllerTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @LocalServerPort
    private Integer port;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private CandidateFixture candidateFixture;

    private static final String BASE_PATH = "/api/v1/candidatos";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void cleanDatabase() {
        // Limpiar la base de datos antes de cada test
        candidatoRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        RestAssured.reset();
    }

    /**
     * Test: Crear un candidato exitosamente
     */
    @Test
    void testCreateCandidate_Success() {
        CandidateDTO candidateDTO = candidateFixture.createValidCandidateDTO();

        // Usamos RestAssured para enviar la solicitud POST
        given()
            .contentType(ContentType.JSON)
            .body(candidateDTO) // Convertimos el DTO a JSON automáticamente
        .when()
            .post("/api/v1/candidatos")
        .then()
            .statusCode(201)
            .body("nombre", equalTo(candidateDTO.getNombre()))
            .body("apellidos", equalTo(candidateDTO.getApellidos()))
            .body("email", equalTo(candidateDTO.getEmail()))
            .body("telefono", equalTo(candidateDTO.getTelefono()))
            .body("tipoDocumento", equalTo(candidateDTO.getTipoDocumento()))
            .body("numeroDocumento", equalTo(candidateDTO.getNumeroDocumento()))
            .body("genero", equalTo(candidateDTO.getGenero()))
            .body("lugarNacimiento", equalTo(candidateDTO.getLugarNacimiento()))
            .body("fechaNacimiento", equalTo(candidateDTO.getFechaNacimiento().toString()))
            .body("direccion", equalTo(candidateDTO.getDireccion()))
            .body("codigoPostal", equalTo(candidateDTO.getCodigoPostal()))
            .body("pais", equalTo(candidateDTO.getPais()))
            .body("localizacion", equalTo(candidateDTO.getLocalizacion()))
            .body("disponibilidadDesde", equalTo(candidateDTO.getDisponibilidadDesde().toString()))
            .body("disponibilidadHasta", equalTo(candidateDTO.getDisponibilidadHasta().toString()))
            .body("id", notNullValue());

        assertThat(candidatoRepository.count()).isEqualTo(1L);
    }

    /**
     * Test: Crear candidato con email duplicado
     */
    @Test
    @DisplayName("POST /api/v1/candidatos - Email duplicado retorna 409 CONFLICT")
    void testCreateCandidate_DuplicatedEmail() {
        // Arrange: Crear candidato inicial
        CandidateDTO candidateDTO = candidateFixture.createValidCandidateDTO();
        Candidato existingCandidate = new Candidato();
        existingCandidate.setNombre(candidateDTO.getNombre());
        existingCandidate.setApellidos(candidateDTO.getApellidos());
        existingCandidate.setEmail(candidateDTO.getEmail());
        existingCandidate.setTelefono(candidateDTO.getTelefono());
        existingCandidate.setTipoDocumento(candidateDTO.getTipoDocumento());
        existingCandidate.setNumeroDocumento(candidateDTO.getNumeroDocumento());
        existingCandidate.setGenero(candidateDTO.getGenero());
        existingCandidate.setLugarNacimiento(candidateDTO.getLugarNacimiento());
        existingCandidate.setFechaNacimiento(candidateDTO.getFechaNacimiento());
        existingCandidate.setDireccion(candidateDTO.getDireccion());
        existingCandidate.setCodigoPostal(candidateDTO.getCodigoPostal());
        existingCandidate.setPais(candidateDTO.getPais());
        existingCandidate.setLocalizacion(candidateDTO.getLocalizacion());
        existingCandidate.setDisponibilidadDesde(candidateDTO.getDisponibilidadDesde());
        existingCandidate.setDisponibilidadHasta(candidateDTO.getDisponibilidadHasta());
        candidatoRepository.save(existingCandidate);

        // Act & Assert: Intentar crear otro candidato con el mismo email
        CandidateDTO duplicateDTO = candidateFixture.createValidCandidateDTO();
        duplicateDTO.setNombre("Otro Nombre");
        
        given()
            .contentType(ContentType.JSON)
            .body(duplicateDTO)
        .when()
            .post(BASE_PATH)
        .then()
            .statusCode(409)
            .body("code", equalTo(409))
            .body("message", equalTo("Email existente"));

        // Verificar que solo hay un candidato en la BD
        assertThat(candidatoRepository.count()).isEqualTo(1);
    }

    /**
     * Test: Validaciones de campos obligatorios
     */
    @Test
    @DisplayName("POST /api/v1/candidatos - Validación de campos obligatorios")
    void testCreateCandidate_ValidationErrors() {
        CandidateDTO invalidDTO = new CandidateDTO();
        invalidDTO.setEmail("invalid-email");
        // Falta nombre, apellidos, etc.

        given()
            .contentType(ContentType.JSON)
            .body(invalidDTO)
        .when()
            .post(BASE_PATH)
        .then()
            .statusCode(400)
            .body("code", equalTo(400))
            .body("message", equalTo("Validation failed"))
            .body("validationErrors", notNullValue())
            .body("validationErrors.nombre", containsString("obligatorio"))
            .body("validationErrors.email", containsString("válido"));
    }

    /**
     * Test: Obtener todos los candidatos
     */
    @Test
    @DisplayName("GET /api/v1/candidatos - Obtener todos los candidatos")
    void testGetAllCandidates_Success() {
        // Arrange: Crear 2 candidatos
        candidateFixture.createAndSaveCandidate("juan@test.com");
        candidateFixture.createAndSaveCandidate("maria@test.com");

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH)
        .then()
            .statusCode(200)
            .body("size()", equalTo(2))
            .body("[0].id", notNullValue())
            .body("[1].id", notNullValue());
    }

    /**
     * Test: Obtener todos los candidatos cuando no hay ninguno
     */
    @Test
    @DisplayName("GET /api/v1/candidatos - Lista vacía retorna 404")
    void testGetAllCandidates_Empty() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH)
        .then()
            .statusCode(404)
            .body("message", equalTo("No se encontraron candidatos"));
    }

    /**
     * Test: Obtener candidato por ID
     */
    @Test
    @DisplayName("GET /api/v1/candidatos/{id} - Obtener candidato por ID")
    void testGetCandidateById_Success() {
        // Arrange
        Candidato savedCandidate = candidateFixture.createAndSaveCandidate("test@email.com");

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}", savedCandidate.getId())
        .then()
            .statusCode(200)
            .body("id", equalTo(savedCandidate.getId()))
            .body("email", equalTo(savedCandidate.getEmail()))
            .body("nombre", equalTo(savedCandidate.getNombre()));
    }

    /**
     * Test: Obtener candidato por ID inexistente
     */
    @Test
    @DisplayName("GET /api/v1/candidatos/{id} - ID inexistente retorna 404")
    void testGetCandidateById_NotFound() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}", 999)
        .then()
            .statusCode(404)
            .body("message", containsString("No se encontró un candidato con el ID dado 999"));
    }

    /**
     * Test: Eliminar candidato
     */
    @Test
    @DisplayName("DELETE /api/v1/candidatos/{id} - Eliminar candidato")
    void testDeleteCandidate_Success() {
        // Arrange
        Candidato savedCandidate = candidateFixture.createAndSaveCandidate("delete@test.com");
        assertThat(candidatoRepository.count()).isEqualTo(1);

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete(BASE_PATH + "/{id}", savedCandidate.getId())
        .then()
            .statusCode(204);

        // Verificar que se eliminó
        assertThat(candidatoRepository.count()).isEqualTo(0);
    }

    /**
     * Test: Eliminar candidato inexistente
     */
    @Test
    @DisplayName("DELETE /api/v1/candidatos/{id} - ID inexistente retorna 404")
    void testDeleteCandidate_NotFound() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete(BASE_PATH + "/{id}", 999)
        .then()
            .statusCode(404)
            .body("message", containsString("No existe un candidato con el ID 999"));
    }
}
