package com.eureka.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eureka.api.dto.ResponseAdjuntoDTO;
import com.eureka.api.exception.NotFoundException;
import com.eureka.api.service.AdjuntoService;


@RestController
@RequestMapping("/api/v1/adjuntos")
public class AdjuntoController {
    private final AdjuntoService candidateService;

    public AdjuntoController(AdjuntoService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseAdjuntoDTO>> findAll() {
        List<ResponseAdjuntoDTO> documentosDTO = candidateService.findAll();
        if (documentosDTO.isEmpty()) {
            throw new NotFoundException("No se encontraron adjuntos");
        }
        return ResponseEntity.ok(documentosDTO);
    }

    @GetMapping("/candidato/{id}")
    public ResponseEntity<List<ResponseAdjuntoDTO>> getDocumentosByCandidatoId(@PathVariable Integer id) {
        List<ResponseAdjuntoDTO> documentosDTO = candidateService.getDocumentosByCandidatoId(id);
        if (documentosDTO.isEmpty()) {
            throw new NotFoundException("No se encontraron adjuntos con el ID de candidato dado " + id);
        }
        return ResponseEntity.ok(documentosDTO);
    }
}
