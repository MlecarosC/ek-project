package com.eureka.api.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CandidateDTO {
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre debe tener como máximo 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido debe tener como máximo 50 caracteres")
    private String apellidos;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe ser válido")
    @Size(max = 150, message = "El correo electrónico debe tener como máximo 150 caracteres")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono debe tener como máximo 20 caracteres")
    private String telefono;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 15, message = "El tipo de documento debe tener como máximo 15 caracteres")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(max = 30, message = "El número de documento debe tener como máximo 30 caracteres")
    private String numeroDocumento;

    @NotBlank(message = "El género es obligatorio")
    @Size(max = 20, message = "El género debe tener como máximo 20 caracteres")
    private String genero;

    @NotBlank(message = "El lugar de nacimiento es obligatorio")
    @Size(max = 200, message = "El lugar de nacimiento debe tener como máximo 200 caracteres")
    private String lugarNacimiento;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser en el futuro")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección debe tener como máximo 200 caracteres")
    private String direccion;

    @NotBlank(message = "El código postal es obligatorio")
    @Size(max = 20, message = "El código postal debe tener como máximo 20 caracteres")
    private String codigoPostal;

    @NotBlank(message = "El país es obligatorio")
    @Size(max = 50, message = "El país debe tener como máximo 50 caracteres")
    private String pais;

    @NotBlank(message = "La localización es obligatoria")
    @Size(max = 150, message = "La localización debe tener como máximo 150 caracteres")
    private String localizacion;

    @NotNull(message = "La fecha de inicio de disponibilidad es obligatoria")
    private LocalDate disponibilidadDesde;

    @NotNull(message = "La fecha de fin de disponibilidad es obligatoria")
    private LocalDate disponibilidadHasta;
}
