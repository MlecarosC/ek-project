package com.eureka.api.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eureka.api.model.Adjunto;
import com.eureka.api.repository.AdjuntoRepository;

@Component
public class AdjuntoFixture {

    @Autowired
    private AdjuntoRepository adjuntoRepository;

    /**
     * Crea y guarda un adjunto en la base de datos.
     */
    public Adjunto createAndSaveAdjunto(Integer candidatoId, String extension, String nombreArchivo) {
        Adjunto adjunto = new Adjunto();
        adjunto.setCandidatoId(candidatoId);
        adjunto.setExtension(extension);
        adjunto.setNombreArchivo(nombreArchivo);
        return adjuntoRepository.save(adjunto);
    }

    /**
     * Crea múltiples adjuntos para un mismo candidato.
     */
    public void createMultipleAdjuntosForCandidato(Integer candidatoId, int cantidad) {
        for (int i = 1; i <= cantidad; i++) {
            String extension = i % 2 == 0 ? "pdf" : "jpg";
            String nombreArchivo = "documento_" + candidatoId + "_" + i + "." + extension;
            createAndSaveAdjunto(candidatoId, extension, nombreArchivo);
        }
    }
}
