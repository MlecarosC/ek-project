package com.eureka.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.eureka.api.clients.AdjuntoFeignClient;
import com.eureka.api.dto.AdjuntoDTO;
import com.eureka.api.dto.CandidatoConAdjuntosDTO;
import com.eureka.api.dto.CandidatoDTO;
import com.eureka.api.exception.NotFoundException;
import com.eureka.api.exception.UniqueEmailException;
import com.eureka.api.model.Candidato;
import com.eureka.api.repository.CandidatoRepository;

import feign.FeignException;

@Service
public class CandidatoService {
    private final CandidatoRepository candidatoRepository;
    private final ModelMapper modelMapper;
    private final AdjuntoFeignClient adjuntoFeignClient;

    public CandidatoService(
            CandidatoRepository candidateRepository, 
            ModelMapper modelMapper,
            AdjuntoFeignClient adjuntoFeignClient) {
        this.candidatoRepository = candidateRepository;
        this.modelMapper = modelMapper;
        this.adjuntoFeignClient = adjuntoFeignClient;
    }

    public CandidatoDTO save(CandidatoDTO candidateDTO) {
        if (candidatoRepository.existsByEmail(candidateDTO.getEmail())) {
            throw new UniqueEmailException("Email existente");
        }
        Candidato candidate = modelMapper.map(candidateDTO, Candidato.class);
        return modelMapper.map(candidatoRepository.save(candidate), CandidatoDTO.class);
    }

    public List<CandidatoDTO> findAll() {
        return candidatoRepository.findAll().stream()
            .map(candidate -> modelMapper.map(candidate, CandidatoDTO.class))
            .collect(Collectors.toList());
    }

    public Optional<CandidatoDTO> findById(Integer id) {
        return candidatoRepository.findById(id)
            .map(candidate -> modelMapper.map(candidate, CandidatoDTO.class));
    }

    public void deleteById(Integer id) {
        Candidato candidate = candidatoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No existe un candidato con el ID " + id));
        candidatoRepository.delete(candidate);
    }

    /**
     * Obtiene un candidato con sus adjuntos asociados
     */
    public Optional<CandidatoConAdjuntosDTO> findByIdWithAdjuntos(Integer id) {
        return candidatoRepository.findById(id).map(candidato -> {
            CandidatoConAdjuntosDTO dto = modelMapper.map(candidato, CandidatoConAdjuntosDTO.class);
            
            try {
                List<AdjuntoDTO> adjuntos = adjuntoFeignClient.getAdjuntosByCandidatoId(id);
                dto.setAdjuntos(adjuntos);
            } catch (FeignException.NotFound error) {
                dto.setAdjuntos(new ArrayList<>());
            }
            
            return dto;
        });
    }

    /**
     * Obtiene todos los candidatos con sus adjuntos.
     */
    public List<CandidatoConAdjuntosDTO> findAllWithAdjuntos() {
        List<Candidato> candidatos = candidatoRepository.findAll();
        
        // Obtener TODOS los adjuntos en una sola llamada
        List<AdjuntoDTO> todosLosAdjuntos;
        try {
            todosLosAdjuntos = adjuntoFeignClient.getAllAdjuntos();
        } catch (FeignException.NotFound e) {
            // Si no hay adjuntos, usar lista vacía
            todosLosAdjuntos = new ArrayList<>();
        }
        
        // Agrupar adjuntos por candidatoId
        Map<Integer, List<AdjuntoDTO>> adjuntosPorCandidato = todosLosAdjuntos.stream()
            .collect(Collectors.groupingBy(AdjuntoDTO::getCandidatoId));
        
        // Mapear candidatos con sus adjuntos
        return candidatos.stream()
            .map(candidato -> {
                CandidatoConAdjuntosDTO dto = modelMapper.map(candidato, CandidatoConAdjuntosDTO.class);
                
                // Obtener adjuntos del mapa (o lista vacía si no tiene)
                List<AdjuntoDTO> adjuntos = adjuntosPorCandidato.getOrDefault(
                    candidato.getId(), 
                    new ArrayList<>()
                );
                dto.setAdjuntos(adjuntos);
                
                return dto;
            })
            .collect(Collectors.toList());
    }
}
