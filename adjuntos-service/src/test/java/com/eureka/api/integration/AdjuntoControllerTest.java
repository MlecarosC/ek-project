package com.eureka.api.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.eureka.api.config.BaseConfig;
import com.eureka.api.stubs.CandidatoWireMockStubs;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

import io.restassured.http.ContentType;

@Testcontainers
public class AdjuntoControllerTest extends BaseConfig {

    @Container
    @ServiceConnection
    protected static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().port(8089))
        .build();
    
    @BeforeEach
    void setupMocks() {
        CandidatoWireMockStubs.setupAllStubs(wireMockServer);
    }

    /**
     * Test: Obtener todos los adjuntos
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos - Obtener todos los candidatos con sus adjuntos")
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
            .body("size()", equalTo(2))
            .body("[0].candidato.id", notNullValue())
            .body("[0].adjuntos.size()", equalTo(2))
            .body("[1].adjuntos.size()", equalTo(3));

        assertThat(adjuntoRepository.count()).isEqualTo(5);
        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/candidatos")));
    }

    /**
     * Test: Obtener todos los adjuntos cuando no hay ninguno
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos - Lista vacía retorna 404")
    void testGetAllAdjuntos_Empty() {
        // Configurar stub específico para este test
        CandidatoWireMockStubs.stubNoCandidatos(wireMockServer);

        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH)
        .then()
            .statusCode(404)
            .body("message", equalTo("No se encontraron candidatos con documentos asociados"));
    }

    /**
     * Test: Obtener adjuntos por ID de candidato
     */
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
            .body("candidato.id", equalTo(candidatoId))
            .body("candidato.nombre", equalTo("Juan"))
            .body("adjuntos.size()", equalTo(3))
            .body("adjuntos[0].extension", notNullValue())
            .body("adjuntos[0].nombreArchivo", notNullValue());
        
        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/candidatos/1")));
    }

    /**
     * Test: Obtener adjuntos de un candidato que no existe
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Candidato inexistente retorna 404")
    void testGetAdjuntosByCandidatoId_CandidateNotFound() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 999)
        .then()
            .statusCode(404)
            .body("message", equalTo("No se encontraron candidatos con el ID dado 999"));
    }

    /**
     * Test: Obtener adjuntos de un candidato sin documentos
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Candidato sin adjuntos retorna 404")
    void testGetAdjuntosByCandidatoId_NoAttachments() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 1)
        .then()
            .statusCode(404)
            .body("message", equalTo("El candidato con ID 1 no tiene adjuntos asociados"));
    }

    /**
     * Test: Verificar estructura completa de la respuesta
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Verificar estructura de respuesta")
    void testGetAdjuntosByCandidatoId_ResponseStructure() {
        // Arrange
        Integer candidatoId = 5;
        adjuntoFixture.createAndSaveAdjunto(candidatoId, "pdf", "cv_completo.pdf");

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", candidatoId)
        .then()
            .statusCode(200)
            .body("candidato.id", equalTo(candidatoId))
            .body("candidato.nombre", equalTo("Ana"))
            .body("candidato.apellidos", equalTo("Martínez"))
            .body("candidato.email", equalTo("ana.martinez@test.com"))
            .body("adjuntos.size()", equalTo(1))
            .body("adjuntos[0].extension", equalTo("pdf"))
            .body("adjuntos[0].nombreArchivo", equalTo("cv_completo.pdf"));
    }

    /**
     * Test: Verificar que diferentes candidatos tienen sus propios documentos
     */
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
            .body("adjuntos.size()", equalTo(2))
            .body("candidato.nombre", equalTo("Juan"));

        // Act & Assert - Candidato 2
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 2)
        .then()
            .statusCode(200)
            .body("adjuntos.size()", equalTo(3))
            .body("candidato.nombre", equalTo("Maria"));

        // Act & Assert - Candidato 3
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 3)
        .then()
            .statusCode(200)
            .body("adjuntos.size()", equalTo(1))
            .body("candidato.nombre", equalTo("Carlos"));

        // Verificar
        assertThat(adjuntoRepository.count()).isEqualTo(6);
        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/candidatos/1")));
        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/candidatos/2")));
        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/api/v1/candidatos/3")));
    }
}
