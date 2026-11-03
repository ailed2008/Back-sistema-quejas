package com.banco.app_quejas.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("TARJETA_CREDITO")

public class QuejaTarjetaCredito extends Queja{
    private String lugarSuceso;

    private LocalDateTime horaTransaccion;

    public QuejaTarjetaCredito(String folio, LocalDate fechaRecepcion, String numCliente,
                           BigDecimal montoReclamado, Prioridad prioridad,
                           String lugarSuceso, LocalDateTime horaTransaccion){
        super(folio, fechaRecepcion, numCliente, montoReclamado, prioridad);
        this.lugarSuceso = lugarSuceso;
        this.horaTransaccion = horaTransaccion;

    }
    @Override
    public void validarRequisitosFormales() {
        if (lugarSuceso == null || lugarSuceso.isBlank()) {
            throw new IllegalArgumentException("El lugar del suceso es obligatorio para quejas de Tarjeta de Crédito.");
        }
    }

}
