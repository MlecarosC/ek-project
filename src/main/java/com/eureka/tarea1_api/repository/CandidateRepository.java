package com.eureka.tarea1_api.repository;

import com.eureka.tarea1_api.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    boolean existsByEmail(String email);
}
