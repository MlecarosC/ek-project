package com.eureka.api.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CandidatoConAdjuntosDTO extends CandidatoDTO {
    private List<AdjuntoDTO> adjuntos;
}
