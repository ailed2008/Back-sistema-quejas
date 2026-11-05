package com.banco.app_quejas.dto;

import com.banco.app_quejas.entity.Prioridad;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for incoming credit card complaint requests.
 * Input validation is handled here to keep the domain model clean.
 */
public record QuejaTarjetaCreditoRequest(

        @NotBlank(message = "Folio is required")
        String folio,

        @NotBlank(message = "Customer number is required")
        String numCliente,

        @NotNull(message = "Claimed amount is required")
        BigDecimal montoReclamado,

        @NotNull(message = "Priority is required")
        Prioridad prioridad,

        @NotBlank(message = "Incident location is required")
        String lugarSuceso,

        @NotNull(message = "Transaction time is required")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime horaTransaccion

) {}
