package com.pdv.api.sale.controller;

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
class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateSaleAndUpdateStock() throws Exception {
        String productRequest = "{\n" +
                "  \"name\": \"Produto Venda\",\n" +
                "  \"sku\": \"SALE-001\",\n" +
                "  \"barcode\": \"9988776655443\",\n" +
                "  \"description\": \"Produto para venda\",\n" +
                "  \"unit\": \"UN\",\n" +
                "  \"quantityPerBox\": 1,\n" +
                "  \"weight\": 0.50,\n" +
                "  \"brand\": \"Marca Venda\",\n" +
                "  \"category\": \"Categoria Venda\",\n" +
                "  \"cost\": 5.00,\n" +
                "  \"price\": 15.00,\n" +
                "  \"stockQty\": 50,\n" +
                "  \"active\": true,\n" +
                "  \"status\": \"ATIVO\"\n" +
                "}";

        String productResult = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String productId = productResult.replaceAll(".*\"id\":(\\d+).*", "$1");

        String saleRequest = "{\n" +
                "  \"createdBy\": \"admin\",\n" +
                "  \"paymentMethod\": \"DINHEIRO\",\n" +
                "  \"customerName\": \"Cliente teste\",\n" +
                "  \"discount\": 5.00,\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"productId\": " + productId + ",\n" +
                "      \"quantity\": 2,\n" +
                "      \"unitPrice\": 15.00,\n" +
                "      \"discount\": 0.50\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mockMvc.perform(post("/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saleRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.createdBy").value("admin"))
                .andExpect(jsonPath("$.items[0].productId").value(Integer.parseInt(productId)))
                .andExpect(jsonPath("$.totalAmount").value(24.50));

        mockMvc.perform(get("/api/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].createdBy").value("admin"));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stockQty").value(48));
    }
}
