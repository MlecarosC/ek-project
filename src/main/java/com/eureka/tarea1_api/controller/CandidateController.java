package com.eureka.tarea1_api.controller;

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

import com.eureka.tarea1_api.dto.CandidateDTO;
import com.eureka.tarea1_api.service.CandidateService;


@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }
    
    @PostMapping
    public ResponseEntity<CandidateDTO> save(@RequestBody CandidateDTO candidateDTO) {
        return new ResponseEntity<>(candidateService.save(candidateDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> findAll() {
        List<CandidateDTO> candidatesDTO = candidateService.findAll();
        if (candidatesDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(candidatesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> findById(@PathVariable Integer id) {
        return candidateService.findById(id).map(
            ResponseEntity::ok
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        if (candidateService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
