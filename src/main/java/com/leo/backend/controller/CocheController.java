package com.leo.backend.controller;

import com.leo.backend.model.Coche;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // IMPORTANTE: Esto permite que el Front y el Back se comuniquen
public class CocheController {

    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";

    private final Map<String, UserCredentials> credencialesPorUsuario = Map.of(
        "usuario", new UserCredentials("password", ROLE_USER),
        "admin", new UserCredentials("pass123", ROLE_ADMIN)
    );

    private final Map<String, SessionInfo> sesionesActivas = new ConcurrentHashMap<>();
    private final Map<String, Coche> inventario = new ConcurrentHashMap<>();
    private final AtomicInteger secuenciaIds = new AtomicInteger(4);

    public CocheController() {
        inventario.put("1", new Coche("1", "Toyota", "Corolla", 25000, true));
        inventario.put("2", new Coche("2", "Seat", "Ibiza", 18000, true));
        inventario.put("3", new Coche("3", "Tesla", "Model 3", 45000, false));
        inventario.put("4", new Coche("4", "Audi", "A3", 32000, true));
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {

        UserCredentials credenciales = credencialesPorUsuario.get(loginRequest.username().trim());
        if (credenciales == null || !credenciales.password().equals(loginRequest.password())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }

        String token = UUID.randomUUID().toString();
        String username = loginRequest.username().trim();
        sesionesActivas.put(token, new SessionInfo(username, credenciales.role()));
        return new LoginResponse(token, username, credenciales.role());
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String token = extraerToken(authorizationHeader);
        sesionesActivas.remove(token);
    }

    @GetMapping("/coches")
    public List<Coche> getTodosLosCoches(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        validarSesion(authorizationHeader);
        List<Coche> coches = new ArrayList<>(inventario.values());
        coches.sort(Comparator.comparingInt(coche -> parsearId(coche.getId())));
        return coches;
    }

    @GetMapping("/coches/{id}")
    public Coche getCochePorId(@PathVariable String id, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        validarSesion(authorizationHeader);
        Coche coche = inventario.get(id);
        if (coche == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coche no encontrado");
        }

        return coche;
    }

    @PostMapping("/coches")
    @ResponseStatus(HttpStatus.CREATED)
    public Coche crearCoche(@Valid @RequestBody CocheRequest cocheRequest,
                            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        validarSesionAdmin(authorizationHeader);

        String id = String.valueOf(secuenciaIds.incrementAndGet());
        Coche nuevoCoche = new Coche(
            id,
            cocheRequest.marca().trim(),
            cocheRequest.modelo().trim(),
            cocheRequest.precio(),
            cocheRequest.enStock()
        );

        inventario.put(id, nuevoCoche);
        return nuevoCoche;
    }

    @PutMapping("/coches/{id}")
    public Coche actualizarCoche(@PathVariable String id,
                                 @Valid @RequestBody CocheRequest cocheRequest,
                                 @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        validarSesionAdmin(authorizationHeader);

        if (!inventario.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coche no encontrado");
        }

        Coche cocheActualizado = new Coche(
            id,
            cocheRequest.marca().trim(),
            cocheRequest.modelo().trim(),
            cocheRequest.precio(),
            cocheRequest.enStock()
        );

        inventario.put(id, cocheActualizado);
        return cocheActualizado;
    }

    @DeleteMapping("/coches/{id}")
    public void eliminarCoche(@PathVariable String id,
                              @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        validarSesionAdmin(authorizationHeader);
        Coche eliminado = inventario.remove(id);
        if (eliminado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coche no encontrado");
        }
    }

    private SessionInfo validarSesion(String authorizationHeader) {
        String token = extraerToken(authorizationHeader);
        SessionInfo sessionInfo = sesionesActivas.get(token);
        if (sessionInfo == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesion no valida");
        }

        return sessionInfo;
    }

    private void validarSesionAdmin(String authorizationHeader) {
        SessionInfo sessionInfo = validarSesion(authorizationHeader);
        if (!ROLE_ADMIN.equals(sessionInfo.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permisos insuficientes");
        }
    }

    private String extraerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token no proporcionado");
        }

        String token = authorizationHeader.substring("Bearer ".length()).trim();
        if (token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token no proporcionado");
        }

        return token;
    }

    private int parsearId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException ex) {
            return Integer.MAX_VALUE;
        }
    }

    public record CocheRequest(
        @NotBlank(message = "La marca es obligatoria") String marca,
        @NotBlank(message = "El modelo es obligatorio") String modelo,
        @PositiveOrZero(message = "El precio no puede ser negativo") int precio,
        boolean enStock
    ) {}

    public record LoginRequest(
        @NotBlank(message = "El usuario es obligatorio") String username,
        @NotBlank(message = "La contrasena es obligatoria") String password
    ) {}

    public record LoginResponse(String token, String username, String role) {}

    private record UserCredentials(String password, String role) {}
    private record SessionInfo(String username, String role) {}
}