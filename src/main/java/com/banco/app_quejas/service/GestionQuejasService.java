package com.banco.app_quejas.service;

import com.banco.app_quejas.dto.QuejaTarjetaCreditoRequest;
import com.banco.app_quejas.entity.*;
import com.banco.app_quejas.repository.QuejaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GestionQuejasService {

    private final QuejaRepository quejaRepository;

    public GestionQuejasService(QuejaRepository quejaRepository) {
        this.quejaRepository = quejaRepository;
    }

    @Transactional
    public void registrarQuejaTarjeta(QuejaTarjetaCreditoRequest dto) {
        Objects.requireNonNull(dto, "El DTO de la queja no puede ser nulo");

        // El VO valida el formato del folio al construirse
        Folio folio = new Folio(dto.folio());

        if (quejaRepository.existsById(folio)) {
            throw new IllegalArgumentException("Ya existe una queja registrada con el folio: " + folio);
        }

        QuejaTarjetaCredito queja = new QuejaTarjetaCredito(
            folio,
            LocalDate.now(),
            dto.numCliente(),
            dto.montoReclamado(),
            dto.prioridad(),
            dto.lugarSuceso(),
            dto.horaTransaccion()
        );

        queja.validarRequisitosFormales();
        quejaRepository.save(queja);
    }

    public Optional<Queja> buscarPorFolio(String folioStr) {
        if (folioStr == null || folioStr.isBlank()) return Optional.empty();
        try {
            return quejaRepository.findById(new Folio(folioStr));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public List<Queja> obtenerTodas() {
        return quejaRepository.findAll();
    }

    @Transactional
    public void avanzarEstado(String folioStr, EstadoQueja nuevoEstado) {
        Queja queja = buscarPorFolio(folioStr)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró la queja con el folio: " + folioStr));
        queja.cambiarEstado(nuevoEstado);
        quejaRepository.save(queja);
    }

    @Transactional
    public void emitirDictamen(String folioStr, String textoDictamen) {
        Queja queja = buscarPorFolio(folioStr)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró la queja con el folio: " + folioStr));
        queja.emitirDictamen(textoDictamen);
        quejaRepository.save(queja);
    }

    @Transactional
    public void eliminarQueja(String folioStr) {
        Folio folio = new Folio(folioStr);
        if (!quejaRepository.existsById(folio)) {
            throw new RuntimeException("No se encontró ninguna queja con el folio: " + folioStr);
        }
        quejaRepository.deleteById(folio);
    }
}
