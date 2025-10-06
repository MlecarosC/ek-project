package com.eureka.api.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.eureka.api.config.BaseConfig;

import io.restassured.http.ContentType;

@Testcontainers
public class AdjuntoControllerTest extends BaseConfig {

    @Container
    @ServiceConnection
    protected static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    /**
     * Test: Obtener todos los adjuntos
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos - Obtener todos los adjuntos")
    void testGetAllAdjuntos_Success() {
        // Arrange: Crear adjuntos de prueba para diferentes candidatos
        adjuntoFixture.createMultipleAdjuntosForCandidato(1, 2);
        adjuntoFixture.createMultipleAdjuntosForCandidato(2, 3);

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH)
        .then()
            .statusCode(200)
            .body("size()", equalTo(5))
            .body("[0].candidatoId", notNullValue())
            .body("[0].extension", notNullValue())
            .body("[0].nombreArchivo", notNullValue());

        // Verificar que hay 5 adjuntos en la BD
        assertThat(adjuntoRepository.count()).isEqualTo(5);
    }

    /**
     * Test: Obtener todos los adjuntos cuando no hay ninguno
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos - Lista vacía retorna 404")
    void testGetAllAdjuntos_Empty() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH)
        .then()
            .statusCode(404)
            .body("message", equalTo("No se encontraron adjuntos"));
    }

    /**
     * Test: Obtener adjuntos por ID de candidato
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Obtener adjuntos por candidato")
    void testGetAdjuntosByCandidatoId_Success() {
        // Arrange: Crear adjuntos para dos candidatos diferentes
        Integer candidatoId = 1;
        adjuntoFixture.createMultipleAdjuntosForCandidato(candidatoId, 3);
        adjuntoFixture.createMultipleAdjuntosForCandidato(2, 2);

        // Act & Assert: Buscar solo los del candidato 1
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", candidatoId)
        .then()
            .statusCode(200)
            .body("size()", equalTo(3))
            .body("[0].candidatoId", equalTo(candidatoId))
            .body("[1].candidatoId", equalTo(candidatoId))
            .body("[2].candidatoId", equalTo(candidatoId));
    }

    /**
     * Test: Obtener adjuntos de un candidato que no tiene documentos
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Candidato sin adjuntos retorna 404")
    void testGetAdjuntosByCandidatoId_Empty() {
        // Arrange: Crear adjuntos solo para el candidato 1
        adjuntoFixture.createMultipleAdjuntosForCandidato(1, 2);

        // Act & Assert: Buscar adjuntos del candidato 999 (no existe)
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 999)
        .then()
            .statusCode(404)
            .body("message", equalTo("No se encontraron adjuntos con el ID de candidato dado 999"));
    }

    /**
     * Test: Verificar estructura completa de la respuesta
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Verificar estructura de respuesta")
    void testGetAdjuntosByCandidatoId_ResponseStructure() {
        // Arrange: Crear un adjunto específico
        Integer candidatoId = 5;
        adjuntoFixture.createAndSaveAdjunto(
            candidatoId, 
            "pdf", 
            "cv_completo.pdf"
        );

        // Act & Assert: Verificar todos los campos de la respuesta
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", candidatoId)
        .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].candidatoId", equalTo(candidatoId))
            .body("[0].extension", equalTo("pdf"))
            .body("[0].nombreArchivo", equalTo("cv_completo.pdf"));
    }

    /**
     * Test: Verificar que diferentes candidatos tienen sus propios documentos
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Aislamiento de documentos por candidato")
    void testGetAdjuntosByCandidatoId_IsolationBetweenCandidates() {
        // Arrange: Crear documentos para tres candidatos diferentes
        adjuntoFixture.createMultipleAdjuntosForCandidato(1, 2);
        adjuntoFixture.createMultipleAdjuntosForCandidato(2, 3);
        adjuntoFixture.createMultipleAdjuntosForCandidato(3, 1);

        // Act & Assert: Verificar que cada candidato tiene solo sus documentos
        
        // Candidato 1 debe tener 2 documentos
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 1)
        .then()
            .statusCode(200)
            .body("size()", equalTo(2));

        // Candidato 2 debe tener 3 documentos
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 2)
        .then()
            .statusCode(200)
            .body("size()", equalTo(3));

        // Candidato 3 debe tener 1 documento
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 3)
        .then()
            .statusCode(200)
            .body("size()", equalTo(1));

        // Total en BD debe ser 6
        assertThat(adjuntoRepository.count()).isEqualTo(6);
    }

}
