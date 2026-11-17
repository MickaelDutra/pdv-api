package com.pdv.api.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record DailySalesReportResponse(
        LocalDate date,
        Long totalSales,
        BigDecimal totalRevenue,
        BigDecimal totalDiscounts,
        BigDecimal netRevenue,
        Long totalItemsSold,
        List<SaleSummary> sales
) {
    public record SaleSummary(
            Long id,
            String customerName,
            String paymentMethod,
            String createdBy,
            BigDecimal totalAmount,
            BigDecimal discount,
            OffsetDateTime createdAt
    ) {
    }
}