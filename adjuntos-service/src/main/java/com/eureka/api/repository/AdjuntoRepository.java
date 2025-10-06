package com.eureka.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.api.model.Adjunto;


public interface AdjuntoRepository extends JpaRepository<Adjunto, Integer> {
    List<Adjunto> findByCandidatoId(Integer candidatoId);
}
