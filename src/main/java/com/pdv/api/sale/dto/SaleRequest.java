package com.pdv.api.sale.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaleRequest(
        @NotBlank(message = "Created by is required")
        String createdBy,

        @NotBlank(message = "Payment method is required")
        String paymentMethod,

        String customerName,

        @NotNull(message = "Discount is required")
        java.math.BigDecimal discount,

        @NotEmpty(message = "At least one sale item is required")
        @Valid
        List<SaleItemRequest> items
) {
}
