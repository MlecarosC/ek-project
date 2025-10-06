package com.eureka.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.eureka.api.dto.CandidateDTO;
import com.eureka.api.exception.NotFoundException;
import com.eureka.api.exception.UniqueEmailException;
import com.eureka.api.model.Candidato;
import com.eureka.api.repository.CandidatoRepository;


@Service
public class CandidateService {
    private final CandidatoRepository candidatoRepository;
    private final ModelMapper modelMapper;

    public CandidateService(CandidatoRepository candidateRepository, ModelMapper modelMapper) {
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
}
