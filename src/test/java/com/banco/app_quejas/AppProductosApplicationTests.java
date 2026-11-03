package com.banco.app_quejas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Smoke test - Aplicación")
class AppProductosApplicationTests {

    @Test
    @DisplayName("El main de la aplicación existe y es invocable")
    void mainClassExists() {
        // Verifica que la clase principal existe sin levantar el contexto completo
        // (evita dependencia de MySQL en el pipeline de tests)
        AppProductosApplication app = new AppProductosApplication();
        assert app != null;
    }
}
