package com.eureka.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.eureka.api.dto.AdjuntoDTO;
import com.eureka.api.model.Adjunto;
import com.eureka.api.repository.AdjuntoRepository;

@Service
public class AdjuntoService {
    private final AdjuntoRepository adjuntoRepository;
    private final ModelMapper modelMapper;

    public AdjuntoService(AdjuntoRepository adjuntoRepository, ModelMapper modelMapper) {
        this.adjuntoRepository = adjuntoRepository;
        this.modelMapper = modelMapper;
    }

    public List<AdjuntoDTO> findAll() {
        return adjuntoRepository.findAll().stream()
            .map(adjunto -> modelMapper.map(adjunto, AdjuntoDTO.class))
            .collect(Collectors.toList());
    }

    public List<AdjuntoDTO> getAdjuntosByCandidatoId(Integer candidatoId) {
        List<Adjunto> adjuntos = adjuntoRepository.findByCandidatoId(candidatoId);
        
        return adjuntos.stream()
            .map(adjunto -> modelMapper.map(adjunto, AdjuntoDTO.class))
            .collect(Collectors.toList());
    }
}
