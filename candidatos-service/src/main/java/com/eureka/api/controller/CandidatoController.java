package com.eureka.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eureka.api.dto.CandidatoDTO;
import com.eureka.api.exception.NotFoundException;
import com.eureka.api.service.CandidatoService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/candidatos")
public class CandidatoController {
    private final CandidatoService candidateService;

    public CandidatoController(CandidatoService candidateService) {
        this.candidateService = candidateService;
    }
    
    @PostMapping
    public ResponseEntity<CandidatoDTO> save(@RequestBody @Valid CandidatoDTO candidateDTO) {
        return new ResponseEntity<>(candidateService.save(candidateDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CandidatoDTO>> findAll() {
        List<CandidatoDTO> candidatesDTO = candidateService.findAll();
        if (candidatesDTO.isEmpty()) {
            throw new NotFoundException("No se encontraron candidatos");
        }
        return ResponseEntity.ok(candidatesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatoDTO> findById(@PathVariable Integer id) {
        return candidateService.findById(id).map(
            ResponseEntity::ok
        ).orElseThrow(
            () -> new NotFoundException("No se encontró un candidato con el ID dado " + id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        candidateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
