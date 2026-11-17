package com.pdv.api.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProductsSoldReportResponse(
        LocalDate startDate,
        LocalDate endDate,
        Long totalItemsSold,
        BigDecimal totalRevenue,
        List<ProductSalesSummary> products
) {
    public record ProductSalesSummary(
            Long productId,
            String productName,
            String sku,
            String category,
            Long quantitySold,
            BigDecimal totalRevenue,
            BigDecimal averagePrice,
            Long numberOfSales
    ) {
    }
}