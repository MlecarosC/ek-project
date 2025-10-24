package com.eureka.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdjuntoCreateDTO {
    @NotNull(message = "El ID del candidato es obligatorio")
    private Integer candidatoId;
    
    @NotBlank(message = "La extensión es obligatoria")
    @Size(max = 5, message = "La extensión debe tener como máximo 5 caracteres")
    private String extension;
    
    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Size(max = 255, message = "El nombre del archivo debe tener como máximo 255 caracteres")
    private String nombreArchivo;
}
