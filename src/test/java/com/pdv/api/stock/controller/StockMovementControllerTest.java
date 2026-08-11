package com.pdv.api.stock.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class StockMovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRegisterStockEntryAndListMovements() throws Exception {
        String productRequest = "{\n" +
                "  \"name\": \"Produto Estoque\",\n" +
                "  \"sku\": \"STOCK-001\",\n" +
                "  \"barcode\": \"1112223334445\",\n" +
                "  \"description\": \"Produto para teste de estoque\",\n" +
                "  \"unit\": \"UN\",\n" +
                "  \"quantityPerBox\": 1,\n" +
                "  \"weight\": 0.50,\n" +
                "  \"brand\": \"Marca Teste\",\n" +
                "  \"category\": \"Categoria Teste\",\n" +
                "  \"cost\": 5.00,\n" +
                "  \"price\": 10.00,\n" +
                "  \"stockQty\": 0,\n" +
                "  \"active\": true,\n" +
                "  \"status\": \"ATIVO\"\n" +
                "}";

        String productResult = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String productId = productResult.replaceAll(".*\"id\":(\\d+).*", "$1");

        String stockRequest = "{\n" +
                "  \"productId\": " + productId + ",\n" +
                "  \"type\": \"ENTRADA\",\n" +
                "  \"quantity\": 20,\n" +
                "  \"createdBy\": \"admin\",\n" +
                "  \"notes\": \"Entrada inicial\"\n" +
                "}";

        mockMvc.perform(post("/api/stock/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stockRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("ENTRADA"))
                .andExpect(jsonPath("$.quantity").value(20));

        mockMvc.perform(get("/api/stock/movements?productId=" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(Integer.parseInt(productId)))
                .andExpect(jsonPath("$[0].type").value("ENTRADA"));
    }
}
