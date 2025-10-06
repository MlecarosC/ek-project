package com.eureka.api.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import com.eureka.api.fixtures.CandidateFixture;
import com.eureka.api.repository.AdjuntoRepository;
import com.eureka.api.repository.CandidatoRepository;

import io.restassured.RestAssured;

/**
 * Clase base para tests de integración.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseConfig {
    @LocalServerPort
    protected Integer port;

    @Autowired
    protected CandidatoRepository candidatoRepository;

    @Autowired
    protected AdjuntoRepository adjuntoRepository;

    @Autowired
    protected CandidateFixture candidateFixture;

    protected static final String BASE_PATH = "/api/v1/candidatos";

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
}