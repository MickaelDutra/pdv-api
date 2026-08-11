package com.pdv.api.sale.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record SaleResponse(
        Long id,
        String customerName,
        String paymentMethod,
        String createdBy,
        BigDecimal totalAmount,
        BigDecimal discount,
        String status,
        OffsetDateTime createdAt,
        List<SaleItemResponse> items
) {
}
