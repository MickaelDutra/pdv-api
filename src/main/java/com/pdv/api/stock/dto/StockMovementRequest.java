package com.pdv.api.stock.dto;

import com.pdv.api.stock.entity.MovementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StockMovementRequest(
        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Movement type is required")
        MovementType type,

        @NotNull(message = "Quantity is required")
        Integer quantity,

        @NotBlank(message = "Created by is required")
        String createdBy,

        String notes
) {
}
