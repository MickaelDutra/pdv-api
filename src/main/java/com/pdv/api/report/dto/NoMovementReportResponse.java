package com.pdv.api.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record NoMovementReportResponse(
        LocalDate sinceDate,
        Integer daysWithoutMovement,
        Long totalProducts,
        Long noMovementCount,
        List<NoMovementProduct> products
) {
    public record NoMovementProduct(
            Long productId,
            String productName,
            String sku,
            String category,
            Integer currentStock,
            BigDecimal unitPrice,
            BigDecimal stockValue,
            OffsetDateTime lastMovementDate,
            Integer daysSinceLastMovement,
            String status
    ) {
    }
}