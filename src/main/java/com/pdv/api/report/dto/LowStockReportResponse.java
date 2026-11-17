package com.pdv.api.report.dto;

import java.math.BigDecimal;
import java.util.List;

public record LowStockReportResponse(
        Integer threshold,
        Long totalProducts,
        Long lowStockCount,
        List<LowStockProduct> products
) {
    public record LowStockProduct(
            Long productId,
            String productName,
            String sku,
            String category,
            Integer currentStock,
            Integer threshold,
            BigDecimal unitPrice,
            BigDecimal stockValue,
            String status
    ) {
    }
}