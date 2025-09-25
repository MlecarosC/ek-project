package com.eureka.tarea1_api.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name = "candidates")
@Getter
@Setter
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 50, nullable = false)
    private String lastName;
    @Column(unique = true, length = 150, nullable = false)
    private String email;
    @Column(length = 20, nullable = false)
    private String phone;
    @Column(length = 15, nullable = false)
    private String documentType;
    @Column(length = 30, nullable = false)
    private String documentNumber;
    @Column(length = 20, nullable = false)
    private String gender;
    @Column(length = 200, nullable = false)
    private String placeOfBirth;
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @Column(length = 200, nullable = false)
    private String address;
    @Column(length = 20, nullable = false)
    private String postalCode;
    @Column(length = 50, nullable = false)
    private String country;
    @Column(length = 150, nullable = false)
    private String localization;
    @Column(nullable = false)
    private LocalDate availableStartDate;
    @Column(nullable = false)
    private LocalDate availableEndDate;
    
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Annex> annexes;
}
