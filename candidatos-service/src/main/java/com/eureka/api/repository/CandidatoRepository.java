package com.eureka.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.api.model.Candidato;


public interface CandidatoRepository extends JpaRepository<Candidato, Integer> {
    boolean existsByEmail(String email);
}
