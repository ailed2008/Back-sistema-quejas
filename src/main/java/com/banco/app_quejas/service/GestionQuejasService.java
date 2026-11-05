package com.banco.app_quejas.service;

import com.banco.app_quejas.dto.QuejaTarjetaCreditoRequest;
import com.banco.app_quejas.entity.*;
import com.banco.app_quejas.exception.ComplaintNotFoundException;
import com.banco.app_quejas.exception.DuplicateFolioException;
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
    public void registerCardComplaint(QuejaTarjetaCreditoRequest dto) {
        Objects.requireNonNull(dto, "Request DTO cannot be null");

        Folio folio = new Folio(dto.folio());

        if (quejaRepository.existsById(folio)) {
            throw new DuplicateFolioException(folio.getValor());
        }

        Monto amount = new Monto(dto.montoReclamado());

        QuejaTarjetaCredito complaint = new QuejaTarjetaCredito(
            folio,
            LocalDate.now(),
            dto.numCliente(),
            amount,
            dto.prioridad(),
            dto.lugarSuceso(),
            dto.horaTransaccion()
        );

        complaint.validateFormalRequirements();
        quejaRepository.save(complaint);
    }

    public Optional<Queja> findByFolio(String folioStr) {
        if (folioStr == null || folioStr.isBlank()) return Optional.empty();
        try {
            return quejaRepository.findById(new Folio(folioStr));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public List<Queja> findAll() {
        return quejaRepository.findAll();
    }

    @Transactional
    public void advanceStatus(String folioStr, EstadoQueja newStatus) {
        Queja complaint = findByFolio(folioStr)
            .orElseThrow(() -> new ComplaintNotFoundException(folioStr));
        complaint.changeStatus(newStatus);
        quejaRepository.save(complaint);
    }

    @Transactional
    public void issueRuling(String folioStr, String rulingText) {
        Queja complaint = findByFolio(folioStr)
            .orElseThrow(() -> new ComplaintNotFoundException(folioStr));
        complaint.issueRuling(rulingText);
        quejaRepository.save(complaint);
    }

    @Transactional
    public void deleteComplaint(String folioStr) {
        Folio folio = new Folio(folioStr);
        if (!quejaRepository.existsById(folio)) {
            throw new ComplaintNotFoundException(folioStr);
        }
        quejaRepository.deleteById(folio);
    }
}
