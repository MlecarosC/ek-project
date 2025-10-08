package com.eureka.api.stubs;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Clase helper para configurar los stubs de WireMock del servicio de candidatos.
 */
public class CandidatoWireMockStubs {

    /**
     * Configura todos los stubs necesarios para los tests de adjuntos.
     */
    public static void setupAllStubs(WireMockExtension wireMockServer) {
        wireMockServer.resetAll();
        
        stubCandidato1(wireMockServer);
        stubCandidato2(wireMockServer);
        stubCandidato3(wireMockServer);
        stubCandidato5(wireMockServer);
        stubAllCandidatos(wireMockServer);
        stubCandidatoNotFound(wireMockServer);
    }

    /**
     * Stub para candidato con ID 1 (Juan Pérez)
     */
    public static void stubCandidato1(WireMockExtension wireMockServer) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/candidatos/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(createCandidatoJson(
                    1, "Juan", "Pérez", "juan.perez@test.com",
                    "+56912345678", "12.345.678-9", "M",
                    "Santiago, Chile", "1990-01-01", "Calle Falsa 123",
                    "8320000", "Santiago, Chile"
                ))));
    }

    /**
     * Stub para candidato con ID 2 (Maria López)
     */
    public static void stubCandidato2(WireMockExtension wireMockServer) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/candidatos/2"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(createCandidatoJson(
                    2, "Maria", "López", "maria.lopez@test.com",
                    "+56987654321", "98.765.432-1", "F",
                    "Valparaíso, Chile", "1985-05-15", "Avenida Principal 456",
                    "2340000", "Valparaíso, Chile"
                ))));
    }

    /**
     * Stub para candidato con ID 3 (Carlos García)
     */
    public static void stubCandidato3(WireMockExtension wireMockServer) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/candidatos/3"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(createCandidatoJson(
                    3, "Carlos", "García", "carlos.garcia@test.com",
                    "+56911122233", "11.222.333-4", "M",
                    "Concepción, Chile", "1992-03-22", "Boulevard Sur 789",
                    "4030000", "Concepción, Chile"
                ))));
    }

    /**
     * Stub para candidato con ID 5 (Ana Martínez)
     */
    public static void stubCandidato5(WireMockExtension wireMockServer) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/candidatos/5"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(createCandidatoJson(
                    5, "Ana", "Martínez", "ana.martinez@test.com",
                    "+56933344455", "22.333.444-5", "F",
                    "La Serena, Chile", "1988-07-10", "Calle Nueva 321",
                    "1700000", "La Serena, Chile"
                ))));
    }

    /**
     * Stub para obtener todos los candidatos
     */
    public static void stubAllCandidatos(WireMockExtension wireMockServer) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/candidatos"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [
                        %s,
                        %s
                    ]
                    """.formatted(
                        createCandidatoJson(1, "Juan", "Pérez", "juan.perez@test.com",
                            "+56912345678", "12.345.678-9", "M",
                            "Santiago, Chile", "1990-01-01", "Calle Falsa 123",
                            "8320000", "Santiago, Chile"),
                        createCandidatoJson(2, "Maria", "López", "maria.lopez@test.com",
                            "+56987654321", "98.765.432-1", "F",
                            "Valparaíso, Chile", "1985-05-15", "Avenida Principal 456",
                            "2340000", "Valparaíso, Chile")
                    ))));
    }

    /**
     * Stub para candidatos no encontrados (404)
     */
    public static void stubCandidatoNotFound(WireMockExtension wireMockServer) {
        wireMockServer.stubFor(get(urlMatching("/api/v1/candidatos/999"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "timestamp": "2025-10-08",
                        "code": 404,
                        "message": "No se encontró un candidato con el ID dado 999",
                        "path": "/api/v1/candidatos/999"
                    }
                """)));
    }

    /**
     * Stub para cuando no hay candidatos (lista vacía)
     */
    public static void stubNoCandidatos(WireMockExtension wireMockServer) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/candidatos"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "timestamp": "2025-10-08",
                        "code": 404,
                        "message": "No se encontraron candidatos",
                        "path": "/api/v1/candidatos"
                    }
                """)));
    }

    /**
     * Método helper para crear JSON de candidato
     */
    private static String createCandidatoJson(
            int id, String nombre, String apellidos, String email,
            String telefono, String numeroDocumento, String genero,
            String lugarNacimiento, String fechaNacimiento, String direccion,
            String codigoPostal, String localizacion) {
        
        return """
            {
                "id": %d,
                "nombre": "%s",
                "apellidos": "%s",
                "email": "%s",
                "telefono": "%s",
                "tipoDocumento": "RUT",
                "numeroDocumento": "%s",
                "genero": "%s",
                "lugarNacimiento": "%s",
                "fechaNacimiento": "%s",
                "direccion": "%s",
                "codigoPostal": "%s",
                "pais": "Chile",
                "localizacion": "%s",
                "disponibilidadDesde": "2025-01-01",
                "disponibilidadHasta": "2025-12-31"
            }
            """.formatted(id, nombre, apellidos, email, telefono, numeroDocumento, 
                         genero, lugarNacimiento, fechaNacimiento, direccion, 
                         codigoPostal, localizacion);
    }
}
