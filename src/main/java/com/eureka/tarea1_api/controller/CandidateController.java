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
import com.eureka.tarea1_api.dto.ResponseAnnexDTO;
import com.eureka.tarea1_api.exception.NotFoundException;
import com.eureka.tarea1_api.service.CandidateService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/candidates")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }
    
    @PostMapping
    public ResponseEntity<CandidateDTO> save(@RequestBody @Valid CandidateDTO candidateDTO) {
        return new ResponseEntity<>(candidateService.save(candidateDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> findAll() {
        List<CandidateDTO> candidatesDTO = candidateService.findAll();
        if (candidatesDTO.isEmpty()) {
            throw new NotFoundException("No candidates found");
        }
        return ResponseEntity.ok(candidatesDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> findById(@PathVariable Integer id) {
        return candidateService.findById(id).map(
            ResponseEntity::ok
        ).orElseThrow(
            () -> new NotFoundException("No candidate with the given ID " + id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        candidateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/annexes")
    public ResponseEntity<List<ResponseAnnexDTO>> getAnnexesByCandidateId(@PathVariable Integer id) {
        List<ResponseAnnexDTO> annexesDTO = candidateService.getAnnexesByCandidateId(id);
        if (annexesDTO.isEmpty()) {
            throw new NotFoundException("No annexes found with the given candidate ID " + id);
        }
        return ResponseEntity.ok(annexesDTO);
    }
}
