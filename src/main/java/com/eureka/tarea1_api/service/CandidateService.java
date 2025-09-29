package com.eureka.tarea1_api.service;

import com.eureka.tarea1_api.dto.CandidateDTO;
import com.eureka.tarea1_api.dto.ResponseDocumentDTO;
import com.eureka.tarea1_api.exception.NotFoundException;
import com.eureka.tarea1_api.exception.UniqueEmailException;
import com.eureka.tarea1_api.model.Candidato;
import com.eureka.tarea1_api.repository.AdjuntoRepository;
import com.eureka.tarea1_api.repository.CandidatoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class CandidateService {
    private final AdjuntoRepository adjuntoRepository;
    private final CandidatoRepository candidatoRepository;
    private final ModelMapper modelMapper;

    public CandidateService(AdjuntoRepository annexRepository, CandidatoRepository candidateRepository, ModelMapper modelMapper) {
        this.adjuntoRepository = annexRepository;
        this.candidatoRepository = candidateRepository;
        this.modelMapper = modelMapper;
    }

    public CandidateDTO save(CandidateDTO candidateDTO) {
        if (candidatoRepository.existsByEmail(candidateDTO.getEmail())) {
            throw new UniqueEmailException("Email existente");
        }
        Candidato candidate = modelMapper.map(candidateDTO, Candidato.class);
        return modelMapper.map(candidatoRepository.save(candidate), CandidateDTO.class);
    }

    public List<CandidateDTO> findAll() {
        return candidatoRepository.findAll().stream().map(
            candidate -> {
                return modelMapper.map(candidate, CandidateDTO.class);
            }
        ).collect(Collectors.toList());
    }

    public Optional<CandidateDTO> findById(Integer id) {
        return candidatoRepository.findById(id).map(
            candidate -> {
                return modelMapper.map(candidate, CandidateDTO.class);
            }
        );
    }

    public void deleteById(Integer id) {
        Candidato candidate = candidatoRepository.findById(id).orElseThrow(
            () -> new NotFoundException("No existe un candidato con el ID " + id)
        );

        candidatoRepository.delete(candidate);
    }

    public List<ResponseDocumentDTO> getDocumentosByCandidatoId(Integer id) {
        return adjuntoRepository.findByCandidatoId(id).stream().map(
            annex -> {
                return modelMapper.map(annex, ResponseDocumentDTO.class);
            }
        ).collect(Collectors.toList());
    }
}
