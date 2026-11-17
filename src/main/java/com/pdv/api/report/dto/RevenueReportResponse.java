package com.pdv.api.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RevenueReportResponse(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalGrossRevenue,
        BigDecimal totalDiscounts,
        BigDecimal totalNetRevenue,
        Long totalSales,
        Long totalItemsSold,
        BigDecimal averageTicket,
        List<DailyRevenue> dailyBreakdown
) {
    public record DailyRevenue(
            LocalDate date,
            BigDecimal grossRevenue,
            BigDecimal discounts,
            BigDecimal netRevenue,
            Long salesCount
    ) {
    }
}