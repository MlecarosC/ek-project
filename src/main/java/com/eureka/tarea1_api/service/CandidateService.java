package com.eureka.tarea1_api.service;

import com.eureka.tarea1_api.dto.CandidateDTO;
import com.eureka.tarea1_api.dto.ResponseAnnexDTO;
import com.eureka.tarea1_api.exception.NotFoundException;
import com.eureka.tarea1_api.model.Candidate;
import com.eureka.tarea1_api.repository.AnnexRepository;
import com.eureka.tarea1_api.repository.CandidateRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class CandidateService {
    private final AnnexRepository annexRepository;
    private final CandidateRepository candidateRepository;
    private final ModelMapper modelMapper;

    public CandidateService(AnnexRepository annexRepository, CandidateRepository candidateRepository, ModelMapper modelMapper) {
        this.annexRepository = annexRepository;
        this.candidateRepository = candidateRepository;
        this.modelMapper = modelMapper;
    }

    public CandidateDTO save(CandidateDTO candidateDTO) {
        Candidate candidate = modelMapper.map(candidateDTO, Candidate.class);
        return modelMapper.map(candidateRepository.save(candidate), CandidateDTO.class);
    }

    public List<CandidateDTO> findAll() {
        return candidateRepository.findAll().stream().map(
            candidate -> {
                return modelMapper.map(candidate, CandidateDTO.class);
            }
        ).collect(Collectors.toList());
    }

    public Optional<CandidateDTO> findById(Integer id) {
        return candidateRepository.findById(id).map(
            candidate -> {
                return modelMapper.map(candidate, CandidateDTO.class);
            }
        );
    }

    public void deleteById(Integer id) {
        Candidate candidate = candidateRepository.findById(id).orElseThrow(
            () -> new NotFoundException("No candidate with the given ID " + id)
        );

        candidateRepository.delete(candidate);
    }

    public List<ResponseAnnexDTO> getAnnexesByCandidateId(Integer id) {
        return annexRepository.findByCandidateId(id).stream().map(
            annex -> {
                return modelMapper.map(annex, ResponseAnnexDTO.class);
            }
        ).collect(Collectors.toList());
    }
}
