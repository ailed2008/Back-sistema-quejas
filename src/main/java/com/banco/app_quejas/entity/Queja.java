package com.banco.app_quejas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "quejas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo_queja", discriminatorType = DiscriminatorType.STRING)
public abstract class Queja {
    @Id
    @Column(nullable = false, updatable = false)
    private String folio;

    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDate fechaRecepcion;

    @Column(name="num_cliente",nullable = false)
    private String numCliente;

    @Column(name = "monto_reclamado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoReclamado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoQueja estado;

    @Column(name = "texto_dictamen", length = 1000)
    private String textoDictamen;

    public Queja(String folio, LocalDate fechaRecepcion, String numCliente,
                 BigDecimal montoReclamado, Prioridad prioridad ){

        this.folio = Objects.requireNonNull(folio, "El folio no puede ser nulo");
        this.fechaRecepcion = Objects.requireNonNull(fechaRecepcion, "La fecha de recepción es obligatoria");
        this.numCliente = Objects.requireNonNull(numCliente, "El número de cliente es obligatorio");
        this.montoReclamado = Objects.requireNonNull(montoReclamado, "El monto reclamado es obligatorio");
        this.prioridad = Objects.requireNonNull(prioridad, "La prioridad es obligatoria");
        this.estado = EstadoQueja.REGISTRADA; // Toda queja nace registrada
    }

    public abstract void validarRequisitosFormales();

    public void cambiarEstado(EstadoQueja nuevoEstado){
        Objects.requireNonNull(nuevoEstado, "El nuevo estado no puede ser nulo");
        this.estado = this.estado.transicionarA(nuevoEstado);
    }

    public void emitirDictamen(String dictamen){
        if (dictamen == null || dictamen.isBlank()) {
            throw new IllegalArgumentException("El texto del dictamen es obligatorio");
        }
        this.textoDictamen = dictamen;
        this.estado = EstadoQueja.DICTAMINADA;
    }


}

