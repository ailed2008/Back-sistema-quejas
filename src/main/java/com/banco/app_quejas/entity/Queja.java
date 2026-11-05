package com.banco.app_quejas.entity;

import com.banco.app_quejas.exception.InvalidStatusTransitionException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "quejas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_queja", discriminatorType = DiscriminatorType.STRING)
public abstract class Queja {

    @EmbeddedId
    private Folio folio;

    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDate receptionDate;

    @Column(name = "num_cliente", nullable = false)
    private String customerNumber;

    @Embedded
    private Monto claimedAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridad prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoQueja estado;

    @Column(name = "texto_dictamen", length = 1000)
    private String ruling;

    public Queja(Folio folio, LocalDate receptionDate, String customerNumber,
                 Monto claimedAmount, Prioridad prioridad) {

        this.folio         = Objects.requireNonNull(folio, "Folio cannot be null");
        this.receptionDate = Objects.requireNonNull(receptionDate, "Reception date cannot be null");
        this.customerNumber= Objects.requireNonNull(customerNumber, "Customer number cannot be null");
        this.claimedAmount = Objects.requireNonNull(claimedAmount, "Claimed amount cannot be null");
        this.prioridad     = Objects.requireNonNull(prioridad, "Priority cannot be null");
        this.estado        = EstadoQueja.REGISTRADA;
    }

    public String getFolioValue() {
        return folio != null ? folio.getValor() : null;
    }

    public abstract void validateFormalRequirements();

    public void changeStatus(EstadoQueja newStatus) {
        Objects.requireNonNull(newStatus, "New status cannot be null");
        try {
            this.estado = this.estado.transicionarA(newStatus);
        } catch (IllegalStateException e) {
            throw new InvalidStatusTransitionException(this.estado.name(), newStatus.name());
        }
    }

    public void issueRuling(String rulingText) {
        if (rulingText == null || rulingText.isBlank()) {
            throw new IllegalArgumentException("Ruling text cannot be empty");
        }
        this.ruling = rulingText;
        this.estado = EstadoQueja.DICTAMINADA;
    }
}
