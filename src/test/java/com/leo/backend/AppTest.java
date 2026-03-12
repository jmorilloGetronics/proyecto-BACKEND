package com.leo.backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testGetSaludo() {
        // Arrange (Preparar) - En este caso es directo
        
        // Act (Actuar)
        String resultado = App.getSaludo();
        
        // Assert (Verificar)
        assertEquals("Hola Mundo!", resultado, "El saludo debería ser exacto");
    }

    @Test
    void testSuma() {
        App miApp = new App();
        
        int resultado = miApp.sumar(2, 3);
        
        // Verificamos que 2 + 3 sea 5
        assertEquals(5, resultado, "La suma de 2 + 3 debe ser 5");
    }
}