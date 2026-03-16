package com.leo.backend;

import com.leo.backend.controller.CocheController;
import com.leo.backend.model.Coche;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testUsuarioPuedeConsultarCatalogo() {
        var loginResponse = cocheController.login(new CocheController.LoginRequest("usuario", "password"));
        assertEquals("USER", loginResponse.role());

        var coches = cocheController.getTodosLosCoches("Bearer " + loginResponse.token());
        assertNotNull(coches);
        assertFalse(coches.isEmpty(), "El catálogo de coches no debería estar vacío");
    }

    @Test
    void testApiSinTokenDaError() {
        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> cocheController.getTodosLosCoches(null)
        );
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void testUsuarioNoPuedeCrearCoches() {
        var loginResponse = cocheController.login(new CocheController.LoginRequest("usuario", "password"));
        String authHeader = "Bearer " + loginResponse.token();

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> cocheController.crearCoche(new CocheController.CocheRequest("BMW", "Serie 1", 28000, true), authHeader)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void testAdminPuedeGestionarCrudDeCoches() {
        var loginResponse = cocheController.login(new CocheController.LoginRequest("admin", "pass123"));
        assertEquals("ADMIN", loginResponse.role());

        String authHeader = "Bearer " + loginResponse.token();

        Coche creado = cocheController.crearCoche(
            new CocheController.CocheRequest("BMW", "Serie 1", 28000, true),
            authHeader
        );

        assertNotNull(creado);
        assertEquals("BMW", creado.getMarca());

        Coche actualizado = cocheController.actualizarCoche(
            creado.getId(),
            new CocheController.CocheRequest("BMW", "Serie 1 Restyling", 29500, false),
            authHeader
        );

        assertEquals("Serie 1 Restyling", actualizado.getModelo());
        assertEquals(29500, actualizado.getPrecio());

        Coche consultado = cocheController.getCochePorId(creado.getId(), authHeader);
        assertEquals(creado.getId(), consultado.getId());

        cocheController.eliminarCoche(creado.getId(), authHeader);

        ResponseStatusException notFound = assertThrows(
            ResponseStatusException.class,
            () -> cocheController.getCochePorId(creado.getId(), authHeader)
        );

        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
    }
}