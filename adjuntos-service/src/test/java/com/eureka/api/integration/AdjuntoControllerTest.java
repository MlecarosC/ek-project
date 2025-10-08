package com.eureka.api.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.eureka.api.config.BaseConfig;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

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
        // Stub para GET /api/v1/candidatos/1
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/candidatos/1"))
            .willReturn(okJson(
            """
                {
                    "id": 1,
                    "nombre": "Juan",
                    "apellidos": "Pérez",
                    "email": "juan.perez@test.com",
                    "telefono": "+56912345678",
                    "tipoDocumento": "RUT",
                    "numeroDocumento": "12.345.678-9",
                    "genero": "M",
                    "lugarNacimiento": "Santiago, Chile",
                    "fechaNacimiento": "1990-01-01",
                    "direccion": "Calle Falsa 123",
                    "codigoPostal": "8320000",
                    "pais": "Chile",
                    "localizacion": "Santiago, Chile",
                    "disponibilidadDesde": "2025-01-01",
                    "disponibilidadHasta": "2025-12-31"
                }
            """)));

        // Stub para GET /api/v1/candidatos
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/candidatos"))
            .willReturn(okJson(
            """
                [
                    {
                        "id": 1,
                        "nombre": "Juan",
                        "apellidos": "Pérez",
                        "email": "juan.perez@test.com",
                        "telefono": "+56912345678",
                        "tipoDocumento": "RUT",
                        "numeroDocumento": "12.345.678-9",
                        "genero": "M",
                        "lugarNacimiento": "Santiago, Chile",
                        "fechaNacimiento": "1990-01-01",
                        "direccion": "Calle Falsa 123",
                        "codigoPostal": "8320000",
                        "pais": "Chile",
                        "localizacion": "Santiago, Chile",
                        "disponibilidadDesde": "2025-01-01",
                        "disponibilidadHasta": "2025-12-31"
                    }
                ]
            """)));
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
            .body("size()", equalTo(2)) // 2 candidatos
            .body("[0].candidato.id", notNullValue())
            .body("[0].adjuntos.size()", equalTo(2))
            .body("[1].adjuntos.size()", equalTo(3));

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
            .body("message", equalTo("No se encontraron candidatos con documentos asociados"));
    }
    /**
     * Test: Obtener adjuntos por ID de candidato
     */
    @Test
    @DisplayName("GET /api/v1/adjuntos/candidato/{id} - Obtener adjuntos por candidato")
    void testGetAdjuntosByCandidatoId_Success() {
        Integer candidatoId = 1;
        adjuntoFixture.createMultipleAdjuntosForCandidato(candidatoId, 3);

        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", candidatoId)
        .then()
            .statusCode(200)
            .body("candidato.id", equalTo(candidatoId))
            .body("adjuntos.size()", equalTo(3))
            .body("adjuntos[0].extension", notNullValue())
            .body("adjuntos[0].nombreArchivo", notNullValue());
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
        Integer candidatoId = 5;
        adjuntoFixture.createAndSaveAdjunto(candidatoId, "pdf", "cv_completo.pdf");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", candidatoId)
        .then()
            .statusCode(200)
            .body("candidato.id", equalTo(candidatoId))
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
        adjuntoFixture.createMultipleAdjuntosForCandidato(1, 2);
        adjuntoFixture.createMultipleAdjuntosForCandidato(2, 3);
        adjuntoFixture.createMultipleAdjuntosForCandidato(3, 1);

        // Candidato 1
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 1)
        .then()
            .statusCode(200)
            .body("adjuntos.size()", equalTo(2));

        // Candidato 2
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 2)
        .then()
            .statusCode(200)
            .body("adjuntos.size()", equalTo(3));

        // Candidato 3
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/candidato/{id}", 3)
        .then()
            .statusCode(200)
            .body("adjuntos.size()", equalTo(1));

        assertThat(adjuntoRepository.count()).isEqualTo(6);
    }

}
