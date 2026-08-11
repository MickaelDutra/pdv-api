package com.pdv.api.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "SKU is required")
        String sku,

        String barcode,
        String description,
        String unit,

        @Min(value = 0, message = "Quantity per box must be non-negative")
        Integer quantityPerBox,

        @DecimalMin(value = "0.0", inclusive = false, message = "Weight must be positive")
        BigDecimal weight,

        String brand,
        String category,

        @NotNull(message = "Cost is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Cost must be non-negative")
        BigDecimal cost,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity must be non-negative")
        Integer stockQty,

        Boolean active,
        String status
) {
}
