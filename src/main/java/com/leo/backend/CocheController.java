package com.leo.backend;

import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // IMPORTANTE: Esto permite que el Front y el Back se comuniquen
public class CocheController {

    @GetMapping("/coches")
    public List<Coche> getTodosLosCoches() {
        return Arrays.asList(
            new Coche("1", "Toyota", "Corolla", 25000, true),
            new Coche("2", "Seat", "Ibiza", 18000, true),
            new Coche("3", "Tesla", "Model 3", 45000, false),
            new Coche("4", "Audi", "A3", 32000, true)
        );
    }

    @GetMapping("/coches/{id}")
    public String getCochePorId(@PathVariable String id) {
        return "Has solicitado los detalles del coche con ID: " + id;
    }
}