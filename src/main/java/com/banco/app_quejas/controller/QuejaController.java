package com.banco.app_quejas.controller;

import com.banco.app_quejas.dto.QuejaTarjetaCreditoRequest;
import com.banco.app_quejas.entity.EstadoQueja;
import com.banco.app_quejas.entity.Queja;
import com.banco.app_quejas.service.GestionQuejasService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quejas")
public class QuejaController {

    private final GestionQuejasService complaintService;

    public QuejaController(GestionQuejasService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping("/tarjeta")
    @PreAuthorize("hasAnyRole('ADMIN','EJECUTIVO')")
    public ResponseEntity<String> registerCardComplaint(@Valid @RequestBody QuejaTarjetaCreditoRequest dto) {
        complaintService.registerCardComplaint(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Complaint registered successfully.");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EJECUTIVO')")
    public ResponseEntity<List<Queja>> findAll() {
        return ResponseEntity.ok(complaintService.findAll());
    }

    @GetMapping("/{folio}")
    @PreAuthorize("hasAnyRole('ADMIN','EJECUTIVO')")
    public ResponseEntity<Queja> findByFolio(@PathVariable String folio) {
        return complaintService.findByFolio(folio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{folio}/estado")
    @PreAuthorize("hasAnyRole('ADMIN','EJECUTIVO')")
    public ResponseEntity<String> advanceStatus(
            @PathVariable String folio,
            @RequestParam EstadoQueja nuevoEstado) {
        complaintService.advanceStatus(folio, nuevoEstado);
        return ResponseEntity.ok("Status updated to: " + nuevoEstado);
    }

    @PatchMapping("/{folio}/dictamen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> issueRuling(
            @PathVariable String folio,
            @RequestBody String rulingText) {
        complaintService.issueRuling(folio, rulingText);
        return ResponseEntity.ok("Ruling issued successfully.");
    }

    @DeleteMapping("/{folio}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteComplaint(@PathVariable String folio) {
        complaintService.deleteComplaint(folio);
        return ResponseEntity.ok("Complaint deleted: " + folio);
    }
}
