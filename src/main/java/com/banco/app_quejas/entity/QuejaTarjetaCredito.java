package com.banco.app_quejas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("TARJETA_CREDITO")
public class QuejaTarjetaCredito extends Queja {

    @Column(name = "lugar_suceso")
    private String incidentLocation;

    @Column(name = "hora_transaccion")
    private LocalDateTime transactionTime;

    public QuejaTarjetaCredito(Folio folio, LocalDate receptionDate, String customerNumber,
                                Monto claimedAmount, Prioridad prioridad,
                                String incidentLocation, LocalDateTime transactionTime) {
        super(folio, receptionDate, customerNumber, claimedAmount, prioridad);
        this.incidentLocation = incidentLocation;
        this.transactionTime  = transactionTime;
    }

    @Override
    public void validateFormalRequirements() {
        if (incidentLocation == null || incidentLocation.isBlank()) {
            throw new IllegalArgumentException("Incident location is required for credit card complaints.");
        }
        if (transactionTime == null) {
            throw new IllegalArgumentException("Transaction time is required.");
        }
    }
}
