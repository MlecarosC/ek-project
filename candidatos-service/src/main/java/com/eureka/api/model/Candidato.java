package com.eureka.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "candidatos")
@Getter
@Setter
public class Candidato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 50, nullable = false)
    private String nombre;
    @Column(length = 50, nullable = false)
    private String apellidos;
    @Column(unique = true, length = 150, nullable = false)
    private String email;
    @Column(length = 20, nullable = false)
    private String telefono;
    @Column(length = 15, nullable = false)
    private String tipoDocumento;
    @Column(length = 30, nullable = false)
    private String numeroDocumento;
    @Column(length = 20, nullable = false)
    private String genero;
    @Column(length = 200, nullable = false)
    private String lugarNacimiento;
    @Column(nullable = false)
    private LocalDate fechaNacimiento;
    @Column(length = 200, nullable = false)
    private String direccion;
    @Column(length = 20, nullable = false)
    private String codigoPostal;
    @Column(length = 50, nullable = false)
    private String pais;
    @Column(length = 150, nullable = false)
    private String localizacion;
    @Column(nullable = false)
    private LocalDate disponibilidadDesde;
    @Column(nullable = false)
    private LocalDate disponibilidadHasta;
}
