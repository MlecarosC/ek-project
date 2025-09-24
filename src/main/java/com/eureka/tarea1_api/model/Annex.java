package com.eureka.tarea1_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "annexes")
@Getter
@Setter
public class Annex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 5, nullable = false)
    private String extension;
    @Column(length = 255, nullable = false)
    private String fileName;

    @ManyToOne
    @JsonIgnore
    private Candidate candidate;
}
