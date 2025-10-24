package com.eureka.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.eureka.api.dto.AdjuntoCreateDTO;
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

    public List<AdjuntoDTO> createAdjuntos(List<AdjuntoCreateDTO> adjuntosDTO) {
        List<Adjunto> adjuntos = adjuntosDTO.stream()
            .map(dto -> {
                Adjunto adjunto = new Adjunto();
                adjunto.setCandidatoId(dto.getCandidatoId());
                adjunto.setExtension(dto.getExtension());
                adjunto.setNombreArchivo(dto.getNombreArchivo());
                return adjunto;
            })
            .collect(Collectors.toList());
        
        List<Adjunto> savedAdjuntos = adjuntoRepository.saveAll(adjuntos);
        
        return savedAdjuntos.stream()
            .map(adjunto -> modelMapper.map(adjunto, AdjuntoDTO.class))
            .collect(Collectors.toList());
    }
}
