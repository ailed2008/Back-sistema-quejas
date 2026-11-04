package com.banco.app_quejas.dto;

import com.banco.app_quejas.entity.Prioridad;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record QuejaTarjetaCreditoRequest(
        @NotBlank(message = "El folio es obligatorio")
        String folio,

        @NotBlank(message = "El número de cliente es obligatorio")
        String numCliente,

        @NotNull(message = "El monto reclamado es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto reclamado debe ser mayor a cero")
        BigDecimal montoReclamado,

        @NotNull(message = "La prioridad es obligatoria")
        Prioridad prioridad,

        @NotBlank(message = "El lugar del suceso es obligatorio")
        String lugarSuceso,

        @NotNull(message = "La hora de la transacción es obligatoria")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime horaTransaccion
) {}