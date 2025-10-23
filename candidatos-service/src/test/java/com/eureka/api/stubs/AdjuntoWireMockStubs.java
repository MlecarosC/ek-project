package com.eureka.api.stubs;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Clase helper para configurar los stubs de WireMock del servicio de adjuntos.
 */
public class AdjuntoWireMockStubs {

    /**
     * Configura todos los stubs necesarios para los tests.
     */
    public static void setupAllStubs(WireMockExtension wireMockServer) {
        wireMockServer.resetAll();
        
        stubGetAllAdjuntos(wireMockServer);
        stubGetAdjuntosByCandidatoId(wireMockServer);
    }

    /**
     * Stub para obtener todos los adjuntos
     */
    public static void stubGetAllAdjuntos(WireMockExtension wireMockServer) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/adjuntos"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [
                        {
                            "candidato": {
                                "id": 1,
                                "nombre": "Test",
                                "apellidos": "User",
                                "email": "test@email.com",
                                "telefono": "+56912345678"
                            },
                            "adjuntos": [
                                {
                                    "id": 1,
                                    "extension": "pdf",
                                    "nombreArchivo": "cv_test.pdf"
                                },
                                {
                                    "id": 2,
                                    "extension": "jpg",
                                    "nombreArchivo": "photo_test.jpg"
                                }
                            ]
                        }
                    ]
                    """)));
    }

    /**
     * Stub para obtener adjuntos de un candidato específico - Con adjuntos
     */
    public static void stubGetAdjuntosByCandidatoId(WireMockExtension wireMockServer) {
        wireMockServer.stubFor(get(urlMatching("/api/v1/adjuntos/candidato/[0-9]+"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "candidato": {
                            "id": 1,
                            "nombre": "Test",
                            "apellidos": "User",
                            "email": "test@email.com",
                            "telefono": "+56912345678",
                            "tipoDocumento": "RUT",
                            "numeroDocumento": "12.345.678-9",
                            "genero": "M",
                            "lugarNacimiento": "Santiago",
                            "fechaNacimiento": "1990-01-01",
                            "direccion": "Calle Test 123",
                            "codigoPostal": "8320000",
                            "pais": "Chile",
                            "localizacion": "Santiago",
                            "disponibilidadDesde": "2025-01-01",
                            "disponibilidadHasta": "2025-12-31"
                        },
                        "adjuntos": [
                            {
                                "id": 1,
                                "extension": "pdf",
                                "nombreArchivo": "cv_test_user.pdf"
                            },
                            {
                                "id": 2,
                                "extension": "jpg",
                                "nombreArchivo": "photo_test_user.jpg"
                            }
                        ]
                    }
                    """)));
    }

    /**
     * Stub para candidato sin adjuntos
     */
    public static void stubCandidatoSinAdjuntos(WireMockExtension wireMockServer, Integer candidatoId) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/adjuntos/candidato/" + candidatoId))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "timestamp": "2025-10-23",
                        "code": 404,
                        "message": "El candidato con ID %d no tiene adjuntos asociados",
                        "path": "/api/v1/adjuntos/candidato/%d"
                    }
                    """.formatted(candidatoId, candidatoId))));
    }

    /**
     * Stub para cuando el servicio de adjuntos no encuentra el candidato
     */
    public static void stubCandidatoNoEncontradoEnAdjuntos(WireMockExtension wireMockServer, Integer candidatoId) {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/adjuntos/candidato/" + candidatoId))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "timestamp": "2025-10-23",
                        "code": 404,
                        "message": "No se encontraron candidatos con el ID dado %d",
                        "path": "/api/v1/adjuntos/candidato/%d"
                    }
                    """.formatted(candidatoId, candidatoId))));
    }

    /**
     * Stub dinámico para un candidato específico con número personalizado de adjuntos
     */
    public static void stubAdjuntosForCandidato(WireMockExtension wireMockServer, Integer candidatoId, String email, int numAdjuntos) {
        StringBuilder adjuntosJson = new StringBuilder();
        for (int i = 1; i <= numAdjuntos; i++) {
            if (i > 1) adjuntosJson.append(",\n");
            adjuntosJson.append("""
                            {
                                "id": %d,
                                "extension": "%s",
                                "nombreArchivo": "documento_%d.%s"
                            }""".formatted(i, i % 2 == 0 ? "jpg" : "pdf", i, i % 2 == 0 ? "jpg" : "pdf"));
        }

        wireMockServer.stubFor(get(urlEqualTo("/api/v1/adjuntos/candidato/" + candidatoId))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "candidato": {
                            "id": %d,
                            "nombre": "Test",
                            "apellidos": "User",
                            "email": "%s",
                            "telefono": "+56912345678",
                            "tipoDocumento": "RUT",
                            "numeroDocumento": "12.345.678-9",
                            "genero": "M",
                            "lugarNacimiento": "Santiago",
                            "fechaNacimiento": "1990-01-01",
                            "direccion": "Calle Test 123",
                            "codigoPostal": "8320000",
                            "pais": "Chile",
                            "localizacion": "Santiago",
                            "disponibilidadDesde": "2025-01-01",
                            "disponibilidadHasta": "2025-12-31"
                        },
                        "adjuntos": [
                    %s
                        ]
                    }
                    """.formatted(candidatoId, email, adjuntosJson.toString()))));
    }
}
