package com.eureka.api.dto;

import java.util.List;

import lombok.Data;


@Data
public class ResponseAdjuntoDTO {
    private CandidatoInfoDTO candidato;
    private List<AdjuntoDTO> adjuntos;
}
