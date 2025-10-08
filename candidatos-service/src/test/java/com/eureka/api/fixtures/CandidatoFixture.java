package com.eureka.api.fixtures;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eureka.api.dto.CandidatoDTO;
import com.eureka.api.model.Candidato;
import com.eureka.api.repository.CandidatoRepository;

@Component
public class CandidatoFixture {

    @Autowired
    private CandidatoRepository candidatoRepository;

    /**
     * Crea y devuelve un CandidateDTO válido.
     */
    public CandidatoDTO createValidCandidateDTO() {
        CandidatoDTO dto = new CandidatoDTO();
        dto.setNombre("Juan");
        dto.setApellidos("Pérez");
        dto.setEmail("juan.perez@test.com");
        dto.setTelefono("+56912345678");
        dto.setTipoDocumento("RUT");
        dto.setNumeroDocumento("12.345.678-9");
        dto.setGenero("M");
        dto.setLugarNacimiento("Santiago, Chile");
        dto.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        dto.setDireccion("Calle Falsa 123");
        dto.setCodigoPostal("8320000");
        dto.setPais("Chile");
        dto.setLocalizacion("Santiago, Chile");
        dto.setDisponibilidadDesde(LocalDate.of(2025, 1, 1));
        dto.setDisponibilidadHasta(LocalDate.of(2025, 12, 31));
        return dto;
    }

    /**
     * Crea y guarda un candidato en la base de datos.
     * Este método también podría utilizarse para insertar un candidato de manera sencilla en las pruebas.
     */
    public Candidato createAndSaveCandidate(String email) {
        Candidato candidato = new Candidato();
        candidato.setNombre("Test");
        candidato.setApellidos("User");
        candidato.setEmail(email);
        candidato.setTelefono("+56912345678");
        candidato.setTipoDocumento("RUT");
        candidato.setNumeroDocumento("12.345.678-9");
        candidato.setGenero("M");
        candidato.setLugarNacimiento("Santiago");
        candidato.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        candidato.setDireccion("Calle Test 123");
        candidato.setCodigoPostal("8320000");
        candidato.setPais("Chile");
        candidato.setLocalizacion("Santiago");
        candidato.setDisponibilidadDesde(LocalDate.of(2025, 1, 1));
        candidato.setDisponibilidadHasta(LocalDate.of(2025, 12, 31));
        return candidatoRepository.save(candidato);
    }
}
