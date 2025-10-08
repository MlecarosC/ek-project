package com.eureka.api.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eureka.api.dto.CandidatoInfoDTO;

/**
 * Feign Client para comunicarse con el servicio de candidatos.
 * El nombre "candidato-service" debe coincidir con spring.application.name 
 * del servicio de candidatos registrado en Eureka.
 */
@FeignClient(name = "candidato-service", url = "${candidato-service.url}")
public interface CandidatoFeignClient {
    @GetMapping("/api/v1/candidatos")
    List<CandidatoInfoDTO> findAll();

    @GetMapping("/api/v1/candidatos/{id}")
    CandidatoInfoDTO getCandidatoById(@PathVariable("id") Integer id);
}
