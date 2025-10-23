package com.eureka.api.dto;

import lombok.Data;

@Data   
public class AdjuntoDTO {
    private Integer id;
    private Integer candidatoId;
    private String extension;
    private String nombreArchivo;
}
