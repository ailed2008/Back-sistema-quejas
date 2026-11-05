package com.banco.app_quejas.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object representing the claimed amount in a complaint.
 * Ensures the amount is always positive and rounded to 2 decimal places.
 */
@Embeddable
public final class Monto implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final BigDecimal MINIMUM = new BigDecimal("0.01");

    @Column(name = "monto_reclamado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    protected Monto() {}

    public Monto(BigDecimal valor) {
        Objects.requireNonNull(valor, "Amount cannot be null");
        if (valor.compareTo(MINIMUM) < 0) {
            throw new IllegalArgumentException(
                "Amount must be greater than zero. Received: " + valor
            );
        }
        this.valor = valor.setScale(2, RoundingMode.HALF_UP);
    }

    @JsonValue
    public BigDecimal getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Monto monto)) return false;
        return Objects.equals(valor, monto.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
