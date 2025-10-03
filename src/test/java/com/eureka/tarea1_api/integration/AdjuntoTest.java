package com.eureka.tarea1_api.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.oneOf;
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

import com.eureka.tarea1_api.fixtures.CandidateFixture;
import com.eureka.tarea1_api.model.Adjunto;
import com.eureka.tarea1_api.model.Candidato;
import com.eureka.tarea1_api.repository.AdjuntoRepository;
import com.eureka.tarea1_api.repository.CandidatoRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AdjuntoTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @LocalServerPort
    private Integer port;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private CandidateFixture candidateFixture;

    @Autowired
    private AdjuntoRepository adjuntoRepository;

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
        adjuntoRepository.deleteAll();
        candidatoRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        RestAssured.reset();
    }

     /**
     * Test: Obtener documentos de un candidato
     * Verifica que se retornen todos los documentos asociados
     */
    @Test
    @DisplayName("GET /api/v1/candidatos/{id}/documentos - Obtener documentos de candidato")
    void testGetDocumentosByCandidatoId_Success() {
        // Arrange: Crear candidato con 2 documentos
        Candidato candidato = candidateFixture.createAndSaveCandidate("test@email.com");
        
        Adjunto doc1 = new Adjunto();
        doc1.setExtension("pdf");
        doc1.setNombreArchivo("cv_test.pdf");
        doc1.setCandidato(candidato);
        adjuntoRepository.save(doc1);

        Adjunto doc2 = new Adjunto();
        doc2.setExtension("jpg");
        doc2.setNombreArchivo("photo_test.jpg");
        doc2.setCandidato(candidato);
        adjuntoRepository.save(doc2);

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}/documentos", candidato.getId())
        .then()
            .statusCode(200)
            .body("size()", equalTo(2))
            .body("[0].candidatoId", equalTo(candidato.getId()))
            .body("[0].extension", is(oneOf("pdf", "jpg")))
            .body("[0].nombreArchivo", notNullValue())
            .body("[1].candidatoId", equalTo(candidato.getId()))
            .body("[1].extension", is(oneOf("pdf", "jpg")));
    }

    /**
     * Test: Obtener documentos de candidato sin documentos
     */
    @Test
    @DisplayName("GET /api/v1/candidatos/{id}/documentos - Candidato sin documentos retorna 404")
    void testGetDocumentosByCandidatoId_EmptyList() {
        // Arrange: Crear candidato sin documentos
        Candidato candidato = candidateFixture.createAndSaveCandidate("nodocs@email.com");

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}/documentos", candidato.getId())
        .then()
            .statusCode(404)
            .body("message", containsString("No se encontraron documentos"));
    }

    /**
     * Test: Obtener documentos de candidato inexistente
     */
    @Test
    @DisplayName("GET /api/v1/candidatos/{id}/documentos - Candidato inexistente retorna 404")
    void testGetDocumentosByCandidatoId_CandidatoNotFound() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}/documentos", 999)
        .then()
            .statusCode(404)
            .body("message", containsString("No se encontraron documentos"));
    }

    /**
     * Test: Verificar que al eliminar un candidato se eliminan sus documentos (CASCADE)
     */
    @Test
    @DisplayName("DELETE candidato elimina sus documentos en cascada")
    void testDeleteCandidate_CascadeDeleteDocuments() {
        // Arrange: Crear candidato con documentos
        Candidato candidato = candidateFixture.createAndSaveCandidate("cascade@email.com");
        
        Adjunto doc1 = new Adjunto();
        doc1.setExtension("pdf");
        doc1.setNombreArchivo("doc1.pdf");
        doc1.setCandidato(candidato);
        adjuntoRepository.save(doc1);

        Adjunto doc2 = new Adjunto();
        doc2.setExtension("docx");
        doc2.setNombreArchivo("doc2.docx");
        doc2.setCandidato(candidato);
        adjuntoRepository.save(doc2);

        // Verificar que existen
        assertThat(candidatoRepository.count()).isEqualTo(1);
        assertThat(adjuntoRepository.count()).isEqualTo(2);

        // Act: Eliminar candidato
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete(BASE_PATH + "/{id}", candidato.getId())
        .then()
            .statusCode(204);

        // Assert: Verificar que los documentos también se eliminaron
        assertThat(candidatoRepository.count()).isEqualTo(0);
        assertThat(adjuntoRepository.count()).isEqualTo(0);
    }

    /**
     * Test: Verificar que diferentes candidatos pueden tener documentos con nombres similares
     */
    @Test
    @DisplayName("Diferentes candidatos pueden tener documentos con nombres similares")
    void testMultipleCandidates_SimilarDocumentNames() {
        // Arrange: Crear 2 candidatos con documentos de nombres similares
        Candidato candidato1 = candidateFixture.createAndSaveCandidate("candidate1@email.com");
        Adjunto doc1 = new Adjunto();
        doc1.setExtension("pdf");
        doc1.setNombreArchivo("curriculum.pdf");
        doc1.setCandidato(candidato1);
        adjuntoRepository.save(doc1);

        Candidato candidato2 = candidateFixture.createAndSaveCandidate("candidate2@email.com");
        Adjunto doc2 = new Adjunto();
        doc2.setExtension("pdf");
        doc2.setNombreArchivo("curriculum.pdf"); // Mismo nombre
        doc2.setCandidato(candidato2);
        adjuntoRepository.save(doc2);

        // Act & Assert: Verificar que cada candidato solo ve sus documentos
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}/documentos", candidato1.getId())
        .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].candidatoId", equalTo(candidato1.getId()));

        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}/documentos", candidato2.getId())
        .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].candidatoId", equalTo(candidato2.getId()));
    }

    /**
     * Test: Verificar tipos de extensiones de documentos
     */
    @Test
    @DisplayName("Documentos con diferentes extensiones se guardan correctamente")
    void testDocuments_DifferentExtensions() {
        // Arrange
        Candidato candidato = candidateFixture.createAndSaveCandidate("extensions@email.com");
        
        String[] extensions = {"pdf", "docx", "jpg", "png", "txt"};
        for (String ext : extensions) {
            Adjunto doc = new Adjunto();
            doc.setExtension(ext);
            doc.setNombreArchivo("document." + ext);
            doc.setCandidato(candidato);
            adjuntoRepository.save(doc);
        }

        // Act & Assert
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(BASE_PATH + "/{id}/documentos", candidato.getId())
        .then()
            .statusCode(200)
            .body("size()", equalTo(5))
            .body("extension", hasItems("pdf", "docx", "jpg", "png", "txt"));
    }
}
