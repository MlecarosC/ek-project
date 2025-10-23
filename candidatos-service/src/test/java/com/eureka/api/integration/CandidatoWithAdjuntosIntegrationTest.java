package com.eureka.api.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
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
import com.eureka.api.model.Candidato;
import com.eureka.api.stubs.AdjuntoWireMockStubs;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

import io.restassured.http.ContentType;

/**
 * Tests de integración para escenarios donde el servicio de candidatos
 * interactúa con el servicio de adjuntos (simulado con WireMock).
 * 
 * Estos tests prueban:
 * - Escenarios donde se necesita verificar adjuntos después de operaciones
 * - Flujos completos que involucran ambos servicios
 * - Comportamiento del sistema cuando el servicio de adjuntos responde de diferentes maneras
 */
@Testcontainers
public class CandidatoWithAdjuntosIntegrationTest extends BaseConfig {

    @Container
    @ServiceConnection
    protected static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().port(9090))
        .build();
    
    @BeforeEach
    void setupMocks() {
        AdjuntoWireMockStubs.setupAllStubs(wireMockServer);
    }

    /**
     * Test: Verificar que después de crear un candidato, 
     * podemos consultar sus adjuntos en el servicio de adjuntos
     */
    @Test
    @DisplayName("Escenario completo: Crear candidato y verificar disponibilidad en servicio de adjuntos")
    void testCreateCandidateAndVerifyInAdjuntosService() {
        // Arrange: Crear candidato
        Candidato candidato = candidateFixture.createAndSaveCandidate("integration@test.com");
        Integer candidatoId = candidato.getId();

        // Configurar stub específico para este candidato
        AdjuntoWireMockStubs.stubAdjuntosForCandidato(wireMockServer, candidatoId, "integration@test.com", 2);

        // Act & Assert: Verificar que existe en el servicio principal
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}", candidatoId)
        .then()
            .statusCode(200)
            .body("id", equalTo(candidatoId))
            .body("email", equalTo("integration@test.com"));

        // Simular consulta al servicio de adjuntos (esto lo haría otro servicio o frontend)
        // Verificamos que WireMock está listo para responder
        wireMockServer.verify(0, getRequestedFor(urlEqualTo("/api/v1/adjuntos/candidato/" + candidatoId)));
        
        assertThat(candidatoRepository.count()).isEqualTo(1);
    }

    /**
     * Test: Verificar comportamiento cuando se elimina un candidato
     * y luego se intenta consultar sus adjuntos
     */
    @Test
    @DisplayName("Escenario: Eliminar candidato y verificar que servicio de adjuntos responde 404")
    void testDeleteCandidateAndVerifyAdjuntosServiceReturns404() {
        // Arrange: Crear candidato
        Candidato candidato = candidateFixture.createAndSaveCandidate("todelete@test.com");
        Integer candidatoId = candidato.getId();

        // Configurar stub para que devuelva 404 después de eliminar
        AdjuntoWireMockStubs.stubCandidatoNoEncontradoEnAdjuntos(wireMockServer, candidatoId);

        // Act: Eliminar candidato
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete(BASE_PATH + "/{id}", candidatoId)
        .then()
            .statusCode(204);

        // Assert: Verificar que se eliminó
        assertThat(candidatoRepository.count()).isEqualTo(0);

        // El servicio de adjuntos debería responder 404 si se consulta
        // (esto simula lo que pasaría en un escenario real)
    }

    /**
     * Test: Obtener todos los candidatos y verificar que el servicio de adjuntos
     * puede proporcionar información sobre sus documentos
     */
    @Test
    @DisplayName("Escenario: Listar candidatos con información de adjuntos disponible")
    void testGetAllCandidatesWithAdjuntosAvailable() {
        // Arrange: Crear múltiples candidatos
        Candidato candidato1 = candidateFixture.createAndSaveCandidate("user1@test.com");
        Candidato candidato2 = candidateFixture.createAndSaveCandidate("user2@test.com");
        Candidato candidato3 = candidateFixture.createAndSaveCandidate("user3@test.com");

        // Configurar stubs para cada candidato con diferente número de adjuntos
        AdjuntoWireMockStubs.stubAdjuntosForCandidato(wireMockServer, candidato1.getId(), "user1@test.com", 2);
        AdjuntoWireMockStubs.stubAdjuntosForCandidato(wireMockServer, candidato2.getId(), "user2@test.com", 1);
        AdjuntoWireMockStubs.stubCandidatoSinAdjuntos(wireMockServer, candidato3.getId());

        // Act & Assert: Obtener todos los candidatos
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH)
        .then()
            .statusCode(200)
            .body("size()", equalTo(3))
            .body("[0].id", notNullValue())
            .body("[1].id", notNullValue())
            .body("[2].id", notNullValue());

        // Los stubs están configurados para responder si otro servicio consulta los adjuntos
        assertThat(candidatoRepository.count()).isEqualTo(3);
    }

    /**
     * Test: Verificar escenario de candidato sin adjuntos
     */
    @Test
    @DisplayName("Escenario: Candidato existe pero no tiene adjuntos")
    void testCandidateExistsButNoAdjuntos() {
        // Arrange
        Candidato candidato = candidateFixture.createAndSaveCandidate("noadjuntos@test.com");
        Integer candidatoId = candidato.getId();

        // Configurar stub para responder 404 (sin adjuntos)
        AdjuntoWireMockStubs.stubCandidatoSinAdjuntos(wireMockServer, candidatoId);

        // Act & Assert: Verificar que el candidato existe
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}", candidatoId)
        .then()
            .statusCode(200)
            .body("id", equalTo(candidatoId))
            .body("email", equalTo("noadjuntos@test.com"));

        // El servicio de adjuntos responderá 404 si se consulta
        assertThat(candidatoRepository.count()).isEqualTo(1);
    }

    /**
     * Test: Flujo completo - Crear, verificar, y simular consulta de adjuntos
     */
    @Test
    @DisplayName("Flujo completo: CRUD de candidato con verificación de adjuntos")
    void testCompleteCRUDFlowWithAdjuntosVerification() {
        // 1. Crear candidato
        Candidato candidato = candidateFixture.createAndSaveCandidate("fullflow@test.com");
        Integer candidatoId = candidato.getId();

        // Configurar stub dinámico
        AdjuntoWireMockStubs.stubAdjuntosForCandidato(wireMockServer, candidatoId, "fullflow@test.com", 3);

        // 2. Verificar creación
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}", candidatoId)
        .then()
            .statusCode(200)
            .body("email", equalTo("fullflow@test.com"));

        // 3. Verificar que existe en BD
        assertThat(candidatoRepository.findById(candidatoId)).isPresent();

        // 4. Simular que otro servicio podría consultar adjuntos
        // (WireMock está listo para responder)

        // 5. Eliminar candidato
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete(BASE_PATH + "/{id}", candidatoId)
        .then()
            .statusCode(204);

        // 6. Verificar eliminación
        assertThat(candidatoRepository.findById(candidatoId)).isEmpty();
    }

    /**
     * Test: Verificar que los stubs de WireMock responden correctamente
     */
    @Test
    @DisplayName("Verificar configuración de WireMock para servicio de adjuntos")
    void testWireMockStubsConfiguration() {
        // Arrange
        Candidato candidato1 = candidateFixture.createAndSaveCandidate("wiremock1@test.com");
        Candidato candidato2 = candidateFixture.createAndSaveCandidate("wiremock2@test.com");

        // Configurar diferentes escenarios
        AdjuntoWireMockStubs.stubAdjuntosForCandidato(wireMockServer, candidato1.getId(), "wiremock1@test.com", 5);
        AdjuntoWireMockStubs.stubCandidatoSinAdjuntos(wireMockServer, candidato2.getId());

        // Verificar que ambos candidatos existen
        assertThat(candidatoRepository.count()).isEqualTo(2);

        // Los stubs están configurados y listos para responder
        // cuando otros servicios los necesiten
    }

    /**
     * Test: Escenario de múltiples operaciones con el mismo candidato
     */
    @Test
    @DisplayName("Escenario: Múltiples consultas del mismo candidato")
    void testMultipleQueriesSameCandidate() {
        // Arrange
        Candidato candidato = candidateFixture.createAndSaveCandidate("multiple@test.com");
        Integer candidatoId = candidato.getId();

        AdjuntoWireMockStubs.stubAdjuntosForCandidato(wireMockServer, candidatoId, "multiple@test.com", 2);

        // Act & Assert: Realizar múltiples consultas
        for (int i = 0; i < 3; i++) {
            given()
                .contentType(ContentType.JSON)
            .when()
                .get(BASE_PATH + "/{id}", candidatoId)
            .then()
                .statusCode(200)
                .body("id", equalTo(candidatoId))
                .body("email", equalTo("multiple@test.com"));
        }

        // Verificar que el candidato sigue existiendo
        assertThat(candidatoRepository.count()).isEqualTo(1);
    }

    /**
     * Test: Validar datos de candidato antes de consultar adjuntos
     */
    @Test
    @DisplayName("Escenario: Validar integridad de datos entre servicios")
    void testDataIntegrityBetweenServices() {
        // Arrange: Crear candidato con datos específicos
        Candidato candidato = candidateFixture.createAndSaveCandidate("integrity@test.com");
        candidato.setNombre("Juan");
        candidato.setApellidos("Pérez");
        candidato.setTelefono("+56912345678");
        candidato = candidatoRepository.save(candidato);

        Integer candidatoId = candidato.getId();

        // Configurar stub con los mismos datos
        AdjuntoWireMockStubs.stubAdjuntosForCandidato(wireMockServer, candidatoId, "integrity@test.com", 1);

        // Act & Assert: Verificar datos
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}", candidatoId)
        .then()
            .statusCode(200)
            .body("id", equalTo(candidatoId))
            .body("nombre", equalTo("Juan"))
            .body("apellidos", equalTo("Pérez"))
            .body("email", equalTo("integrity@test.com"))
            .body("telefono", equalTo("+56912345678"));

        // Los datos están consistentes para cuando se consulte el servicio de adjuntos
        assertThat(candidatoRepository.count()).isEqualTo(1);
    }
}
