package com.pdv.api.finance.dto;

import com.pdv.api.finance.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull(message = "Sale ID is required")
        Long saleId,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @NotNull(message = "Gross amount is required")
        @PositiveOrZero(message = "Gross amount must be positive or zero")
        BigDecimal grossAmount,

        @PositiveOrZero(message = "Discount amount must be positive or zero")
        BigDecimal discountAmount,

        @NotNull(message = "Net amount is required")
        @PositiveOrZero(message = "Net amount must be positive or zero")
        BigDecimal netAmount,

        String transactionId,

        String cardBrand,

        String cardLastDigits,

        String notes
) {
}