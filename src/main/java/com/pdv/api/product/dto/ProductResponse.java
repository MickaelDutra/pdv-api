package com.pdv.api.product.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductResponse(
        Long id,
        String name,
        String sku,
        String barcode,
        String description,
        String unit,
        Integer quantityPerBox,
        BigDecimal weight,
        String brand,
        String category,
        BigDecimal cost,
        BigDecimal price,
        Integer stockQty,
        Boolean active,
        String status,
        OffsetDateTime createdAt
) {
}
