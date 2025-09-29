package com.eureka.tarea1_api.repository;

import com.eureka.tarea1_api.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CandidatoRepository extends JpaRepository<Candidato, Integer> {
    boolean existsByEmail(String email);
}
