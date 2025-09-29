package com.eureka.tarea1_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.tarea1_api.model.Adjunto;


public interface AdjuntoRepository extends JpaRepository<Adjunto, Integer> {
    List<Adjunto> findByCandidatoId(Integer candidatoId);
}
