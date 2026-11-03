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
@CrossOrigin(origins = "http://localhost:4200",allowCredentials = "true")
public class QuejaController {

    private final GestionQuejasService gestionQuejasService;

    public QuejaController(GestionQuejasService gestionQuejasService) {
        this.gestionQuejasService = gestionQuejasService;
    }

    @PostMapping("/tarjeta")
    @PreAuthorize("hasAnyRole('ADMIN','EJECUTIVO')")
    public ResponseEntity<String> registrarQuejaTarjeta(@Valid @RequestBody QuejaTarjetaCreditoRequest dto) {
        try {
            gestionQuejasService.registrarQuejaTarjeta(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("✅ Queja de tarjeta de crédito registrada y guardada en MySQL con éxito.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Error de validación: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Error interno: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EJECUTIVO')")
    public ResponseEntity<List<Queja>> listarTodas() {
        List<Queja> quejas = gestionQuejasService.obtenerTodas();
        return ResponseEntity.ok(quejas);
    }

    @GetMapping("/{folio}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EJECUTIVO')")
    public ResponseEntity<Object> buscarPorFolio(@PathVariable String folio) {
        return gestionQuejasService.buscarPorFolio(folio)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("❌ No se encontró ninguna queja con el folio: " + folio));
    }

    @PatchMapping("/{folio}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'EJECUTIVO')")
    public ResponseEntity<String> cambiarEstado(
            @PathVariable String folio,
            @RequestParam EstadoQueja nuevoEstado) {
        try {
            gestionQuejasService.avanzarEstado(folio, nuevoEstado);
            return ResponseEntity.ok("✅ Estado de la queja actualizado exitosamente a: " + nuevoEstado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Error al cambiar estado: " + e.getMessage());
        }
    }

    @PatchMapping("/{folio}/dictamen")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> emitirDictamen(
            @PathVariable String folio,
            @RequestBody String textoDictamen) {
        try {
            gestionQuejasService.emitirDictamen(folio, textoDictamen);
            return ResponseEntity.ok("✅ Dictamen emitido correctamente y estado actualizado a DICTAMINADA.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Error al emitir dictamen: " + e.getMessage());
        }
    }

    @DeleteMapping("/{folio}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarQueja(@PathVariable String folio){
        try{
            gestionQuejasService.eliminarQueja(folio);
            return ResponseEntity.ok("Queja eliminada exitosamente con folio: "+folio);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al eliminar un folio");
        }
    }
}