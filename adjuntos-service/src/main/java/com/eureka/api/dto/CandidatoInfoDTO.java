package com.eureka.api.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CandidatoInfoDTO {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String tipoDocumento;
    private String numeroDocumento;
    private String genero;
    private String lugarNacimiento;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String codigoPostal;
    private String pais;
    private String localizacion;
    private LocalDate disponibilidadDesde;
    private LocalDate disponibilidadHasta;
}