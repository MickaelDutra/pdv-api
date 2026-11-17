package com.pdv.api.sale.dto;

import com.pdv.api.finance.entity.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record SaleRequest(
        @NotBlank(message = "Created by is required")
        String createdBy,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        String customerName,

        @NotNull(message = "Discount is required")
        BigDecimal discount,

        @NotEmpty(message = "At least one sale item is required")
        @Valid
        List<SaleItemRequest> items
) {
}
