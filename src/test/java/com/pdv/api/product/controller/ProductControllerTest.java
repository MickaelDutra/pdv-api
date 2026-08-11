package com.pdv.api.product.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:pdv-test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateAndRetrieveProduct() throws Exception {
        String requestBody = "{\n" +
                "  \"name\": \"Produto Teste\",\n" +
                "  \"sku\": \"PROD-001\",\n" +
                "  \"barcode\": \"1234567890123\",\n" +
                "  \"description\": \"Descrição do produto\",\n" +
                "  \"unit\": \"UN\",\n" +
                "  \"quantityPerBox\": 10,\n" +
                "  \"weight\": 1.25,\n" +
                "  \"brand\": \"Marca X\",\n" +
                "  \"category\": \"Categoria A\",\n" +
                "  \"cost\": 10.00,\n" +
                "  \"price\": 15.00,\n" +
                "  \"stockQty\": 100,\n" +
                "  \"active\": true,\n" +
                "  \"status\": \"ATIVO\"\n" +
                "}";

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Produto Teste"))
                .andExpect(jsonPath("$.sku").value("PROD-001"));

        mockMvc.perform(get("/api/products?sku=PROD-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Produto Teste"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProduct() throws Exception {
        String requestBody = "{\n" +
                "  \"name\": \"Produto Atualização\",\n" +
                "  \"sku\": \"PROD-002\",\n" +
                "  \"barcode\": \"9876543210987\",\n" +
                "  \"description\": \"Produto original\",\n" +
                "  \"unit\": \"UN\",\n" +
                "  \"quantityPerBox\": 5,\n" +
                "  \"weight\": 0.75,\n" +
                "  \"brand\": \"Marca Y\",\n" +
                "  \"category\": \"Categoria B\",\n" +
                "  \"cost\": 8.00,\n" +
                "  \"price\": 12.00,\n" +
                "  \"stockQty\": 50,\n" +
                "  \"active\": true,\n" +
                "  \"status\": \"ATIVO\"\n" +
                "}";

        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // pega o id criado
        String id = response.replaceAll(".*\"id\":(\\d+).*", "$1");

        String updateBody = "{\n" +
                "  \"name\": \"Produto Atualizado\",\n" +
                "  \"sku\": \"PROD-002\",\n" +
                "  \"barcode\": \"9876543210987\",\n" +
                "  \"description\": \"Produto atualizado\",\n" +
                "  \"unit\": \"UN\",\n" +
                "  \"quantityPerBox\": 5,\n" +
                "  \"weight\": 0.80,\n" +
                "  \"brand\": \"Marca Y\",\n" +
                "  \"category\": \"Categoria B\",\n" +
                "  \"cost\": 9.00,\n" +
                "  \"price\": 13.00,\n" +
                "  \"stockQty\": 60,\n" +
                "  \"active\": true,\n" +
                "  \"status\": \"ATIVO\"\n" +
                "}";

        mockMvc.perform(put("/api/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Atualizado"))
                .andExpect(jsonPath("$.price").value(13.00));
    }
}
