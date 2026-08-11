package com.pdv.api.sale.dto;

import java.math.BigDecimal;

public record SaleItemResponse(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal total,
        BigDecimal discount
) {
}
