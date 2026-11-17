package com.pdv.api.finance.dto;

import com.pdv.api.finance.entity.PaymentMethod;
import com.pdv.api.finance.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentResponse(
        Long id,
        Long saleId,
        PaymentMethod paymentMethod,
        BigDecimal grossAmount,
        BigDecimal discountAmount,
        BigDecimal netAmount,
        BigDecimal paidAmount,
        PaymentStatus status,
        String transactionId,
        String cardBrand,
        String cardLastDigits,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime processedAt
) {
}