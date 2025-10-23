package com.eureka.api.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eureka.api.dto.AdjuntoDTO;

/**
 * Feign Client para comunicarse con el servicio de adjuntos.
 * El nombre "adjunto-service" debe coincidir con spring.application.name 
 * del servicio de adjuntos registrado en Eureka.
 */
@FeignClient(name = "adjunto-service", url = "${adjunto-service.url:}")
public interface AdjuntoFeignClient {
    
    @GetMapping("/api/v1/adjuntos")
    List<AdjuntoDTO> getAllAdjuntos();
    
    @GetMapping("/api/v1/adjuntos/candidato/{candidatoId}")
    List<AdjuntoDTO> getAdjuntosByCandidatoId(@PathVariable("candidatoId") Integer candidatoId);
}