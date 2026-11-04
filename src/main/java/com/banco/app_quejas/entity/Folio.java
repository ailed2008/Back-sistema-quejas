package com.banco.app_quejas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object que representa el folio único de una queja.
 * Formato válido: letras mayúsculas, guion y números. Ej: QTC-001, TARJETA-2026-01
 */
@Embeddable
public final class Folio {

    private static final Pattern FORMATO = Pattern.compile("^[A-Z0-9]+-[A-Z0-9-]+$");

    @Column(name = "folio", nullable = false, updatable = false, length = 50)
    private String valor;

    // Constructor vacío requerido por JPA
    protected Folio() {}

    public Folio(String valor) {
        Objects.requireNonNull(valor, "El folio no puede ser nulo");
        String trimmed = valor.trim().toUpperCase();
        if (trimmed.isBlank()) {
            throw new IllegalArgumentException("El folio no puede estar vacío");
        }
        if (trimmed.length() < 3 || trimmed.length() > 50) {
            throw new IllegalArgumentException("El folio debe tener entre 3 y 50 caracteres");
        }
        if (!FORMATO.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(
                "Formato de folio inválido: '" + valor + "'. Debe seguir el patrón: PREFIJO-NUMERO (ej: QTC-001)"
            );
        }
        this.valor = trimmed;
    }

    public String getValor() {
        return valor;
    }

    /** Hace que Jackson serialice Folio como "QTC-001" en lugar de {"valor":"QTC-001"} */
    @com.fasterxml.jackson.annotation.JsonValue
    public String toJson() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Folio folio)) return false;
        return Objects.equals(valor, folio.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
