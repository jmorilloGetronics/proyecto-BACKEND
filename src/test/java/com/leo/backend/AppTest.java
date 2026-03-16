package com.leo.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        assertNotNull(mockMvc);
    }

    @Test
    void testUsuarioPuedeConsultarCatalogo() throws Exception {
        String token = loginYObtenerToken("usuario", "password", "USER");

        mockMvc.perform(get("/api/coches")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].marca").exists());
    }

    @Test
    void testApiSinTokenDaError() throws Exception {
        mockMvc.perform(get("/api/coches"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Token no proporcionado"));
    }

    @Test
    void testUsuarioNoPuedeCrearCoches() throws Exception {
        String token = loginYObtenerToken("usuario", "password", "USER");

        String payload = """
            {
              "marca": "BMW",
              "modelo": "Serie 1",
              "precio": 28000,
              "enStock": true
            }
            """;

        mockMvc.perform(post("/api/coches")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("Permisos insuficientes"));
    }

    @Test
    void testAdminPuedeGestionarCrudDeCoches() throws Exception {
        String token = loginYObtenerToken("admin", "pass123", "ADMIN");

        String createPayload = """
            {
              "marca": "BMW",
              "modelo": "Serie 1",
              "precio": 28000,
              "enStock": true
            }
            """;

        MvcResult createResult = mockMvc.perform(post("/api/coches")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.marca").value("BMW"))
            .andReturn();

        JsonNode creado = objectMapper.readTree(createResult.getResponse().getContentAsString());
        String idCreado = creado.get("id").asText();

        String updatePayload = """
            {
              "marca": "BMW",
              "modelo": "Serie 1 Restyling",
              "precio": 29500,
              "enStock": false
            }
            """;

        mockMvc.perform(put("/api/coches/{id}", idCreado)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.modelo").value("Serie 1 Restyling"))
            .andExpect(jsonPath("$.precio").value(29500));

        mockMvc.perform(get("/api/coches/{id}", idCreado)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(idCreado));

        mockMvc.perform(delete("/api/coches/{id}", idCreado)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/coches/{id}", idCreado)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Coche no encontrado"));
    }

    @Test
    void testValidacionDeCocheDaBadRequest() throws Exception {
        String token = loginYObtenerToken("admin", "pass123", "ADMIN");

        String invalidPayload = """
            {
              "marca": "",
              "modelo": "",
              "precio": -10,
              "enStock": true
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/coches")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPayload))
            .andExpect(status().isBadRequest())
            .andReturn();

        String body = result.getResponse().getContentAsString();
        assertTrue(body.contains("marca"));
        assertTrue(body.contains("modelo"));
    }

    private String loginYObtenerToken(String username, String password, String expectedRole) throws Exception {
        String payload = objectMapper.writeValueAsString(
            java.util.Map.of("username", username, "password", password)
        );

        MvcResult result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.role").value(expectedRole))
            .andReturn();

        JsonNode loginJson = objectMapper.readTree(result.getResponse().getContentAsString());
        return loginJson.get("token").asText();
    }
}