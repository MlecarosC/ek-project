package com.eureka.tarea1_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.tarea1_api.model.Annex;


public interface AnnexRepository extends JpaRepository<Annex, Integer> {
    List<Annex> findByCandidateId(Integer candidateId);
}
