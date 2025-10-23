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

    @Test
    @DisplayName("GET /api/v1/adjuntos - Obtener todos los adjuntos")
    void testGetAllAdjuntos_Success() {
        // Arrange
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

        assertThat(adjuntoRepository.count()).isEqualTo(5);
    }

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

    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Obtener adjuntos por candidato")
    void testGetAdjuntosByCandidatoId_Success() {
        // Arrange
        Integer candidatoId = 1;
        adjuntoFixture.createMultipleAdjuntosForCandidato(candidatoId, 3);

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", candidatoId)
        .then()
            .statusCode(200)
            .body("size()", equalTo(3))
            .body("[0].candidatoId", equalTo(candidatoId))
            .body("[0].extension", notNullValue())
            .body("[0].nombreArchivo", notNullValue());
    }

    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Candidato sin adjuntos retorna 404")
    void testGetAdjuntosByCandidatoId_NoAttachments() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 999)
        .then()
            .statusCode(404)
            .body("message", equalTo("No se encontraron adjuntos para el candidato con ID 999"));
    }

    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Aislamiento de documentos por candidato")
    void testGetAdjuntosByCandidatoId_IsolationBetweenCandidates() {
        // Arrange
        adjuntoFixture.createMultipleAdjuntosForCandidato(1, 2);
        adjuntoFixture.createMultipleAdjuntosForCandidato(2, 3);
        adjuntoFixture.createMultipleAdjuntosForCandidato(3, 1);

        // Act & Assert - Candidato 1
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 1)
        .then()
            .statusCode(200)
            .body("size()", equalTo(2))
            .body("[0].candidatoId", equalTo(1));

        // Act & Assert - Candidato 2
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 2)
        .then()
            .statusCode(200)
            .body("size()", equalTo(3))
            .body("[0].candidatoId", equalTo(2));

        // Act & Assert - Candidato 3
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 3)
        .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].candidatoId", equalTo(3));

        assertThat(adjuntoRepository.count()).isEqualTo(6);
    }
}
