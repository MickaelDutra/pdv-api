package com.pdv.api.stock.dto;

import com.pdv.api.stock.entity.MovementType;
import java.time.OffsetDateTime;

public record StockMovementResponse(
        Long id,
        Long productId,
        String productName,
        MovementType type,
        Integer quantity,
        String createdBy,
        String notes,
        OffsetDateTime createdAt
) {
}
