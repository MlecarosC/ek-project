package com.eureka.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eureka.api.dto.AdjuntoDTO;
import com.eureka.api.exception.NotFoundException;
import com.eureka.api.service.AdjuntoService;

@RestController
@RequestMapping("/api/v1/adjuntos")
public class AdjuntoController {
    private final AdjuntoService adjuntoService;

    public AdjuntoController(AdjuntoService adjuntoService) {
        this.adjuntoService = adjuntoService;
    }

    @GetMapping
    public ResponseEntity<List<AdjuntoDTO>> findAll() {
        List<AdjuntoDTO> adjuntosDTO = adjuntoService.findAll();
        if (adjuntosDTO.isEmpty()) {
            throw new NotFoundException("No se encontraron adjuntos");
        }
        return ResponseEntity.ok(adjuntosDTO);
    }

    @GetMapping("/candidato/{candidatoId}")
    public ResponseEntity<List<AdjuntoDTO>> getAdjuntosByCandidatoId(@PathVariable Integer candidatoId) {
        List<AdjuntoDTO> adjuntosDTO = adjuntoService.getAdjuntosByCandidatoId(candidatoId);
        if (adjuntosDTO.isEmpty()) {
            throw new NotFoundException("No se encontraron adjuntos para el candidato con ID " + candidatoId);
        }
        return ResponseEntity.ok(adjuntosDTO);
    }
}
