package com.leo.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class AppTest {

    @Autowired
    private CocheController cocheController;

    @Test
    void contextLoads() {
        // Verifica que el controlador se ha creado correctamente
        assertNotNull(cocheController);
    }

    @Test
    void testApiDevuelveCoches() {
        // Verifica que el método del controlador devuelve datos
        var coches = cocheController.getTodosLosCoches();
        assertNotNull(coches);
        assertFalse(coches.isEmpty(), "El catálogo de coches no debería estar vacío");
    }
}