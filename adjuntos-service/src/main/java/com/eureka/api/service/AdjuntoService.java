package com.eureka.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.eureka.api.clients.CandidatoFeignClient;
import com.eureka.api.dto.AdjuntoDTO;
import com.eureka.api.dto.CandidatoInfoDTO;
import com.eureka.api.dto.ResponseAdjuntoDTO;
import com.eureka.api.exception.NotFoundException;
import com.eureka.api.model.Adjunto;
import com.eureka.api.repository.AdjuntoRepository;

import feign.FeignException;


@Service
public class AdjuntoService {
    private final AdjuntoRepository adjuntoRepository;
    private final ModelMapper modelMapper;
    private final CandidatoFeignClient candidatoFeignClient;

    public AdjuntoService(AdjuntoRepository adjuntoRepository, ModelMapper modelMapper, CandidatoFeignClient candidatoFeignClient) {
        this.adjuntoRepository = adjuntoRepository;
        this.modelMapper = modelMapper;
        this.candidatoFeignClient = candidatoFeignClient;
    }

    public List<ResponseAdjuntoDTO> findAll() {
        List<CandidatoInfoDTO> candidatos;
        try {
            candidatos = candidatoFeignClient.findAll();
        } catch (FeignException.NotFound exception) {
            throw new NotFoundException("No se encontraron candidatos con documentos asociados");
        }

        return candidatos.stream().map(candidato -> {
            List<Adjunto> adjuntos = adjuntoRepository.findByCandidatoId(candidato.getId());

            List<AdjuntoDTO> adjuntosSimples = adjuntos.isEmpty() ? new ArrayList<>() : adjuntos.stream()
                .map(adjunto -> modelMapper.map(adjunto, AdjuntoDTO.class))
                .collect(Collectors.toList());

            ResponseAdjuntoDTO response = modelMapper.map(candidato, ResponseAdjuntoDTO.class);
            response.setAdjuntos(adjuntosSimples);

            return response;
        }).collect(Collectors.toList());
    }

    public ResponseAdjuntoDTO getDocumentosByCandidatoId(Integer candidatoId) {
        List<Adjunto> adjuntos;
        CandidatoInfoDTO candidato;
        try {
            candidato = candidatoFeignClient.getCandidatoById(candidatoId);
        } catch (FeignException.NotFound exception) {
            throw new NotFoundException("No se encontraron candidatos con el ID dado " + candidatoId);
        }

        adjuntos = adjuntoRepository.findByCandidatoId(candidatoId);
        if (adjuntos.isEmpty()) {
            throw new NotFoundException("El candidato con ID " + candidatoId + " no tiene adjuntos asociados");
        }

        List<AdjuntoDTO> adjuntosSimples = adjuntos.stream()
            .map(adjunto -> modelMapper.map(adjunto, AdjuntoDTO.class))
            .collect(Collectors.toList());
        
        ResponseAdjuntoDTO resultado = modelMapper.map(candidato, ResponseAdjuntoDTO.class);
        resultado.setAdjuntos(adjuntosSimples);
        
        return resultado;
    }
}
