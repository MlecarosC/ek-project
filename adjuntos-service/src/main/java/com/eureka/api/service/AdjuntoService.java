package com.eureka.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.eureka.api.dto.ResponseDocumentDTO;
import com.eureka.api.repository.AdjuntoRepository;


@Service
public class AdjuntoService {
    private final AdjuntoRepository adjuntoRepository;
    private final ModelMapper modelMapper;

    public AdjuntoService(AdjuntoRepository adjuntoRepository, ModelMapper modelMapper) {
        this.adjuntoRepository = adjuntoRepository;
        this.modelMapper = modelMapper;
    }

    public List<ResponseDocumentDTO> findAll() {
        return adjuntoRepository.findAll().stream().map(
            annex -> {
                return modelMapper.map(annex, ResponseDocumentDTO.class);
            }
        ).collect(Collectors.toList());
    }

    public List<ResponseDocumentDTO> getDocumentosByCandidatoId(Integer id) {
        return adjuntoRepository.findByCandidatoId(id).stream().map(
            annex -> {
                return modelMapper.map(annex, ResponseDocumentDTO.class);
            }
        ).collect(Collectors.toList());
    }
}
