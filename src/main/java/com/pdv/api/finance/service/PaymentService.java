package com.pdv.api.finance.service;

import com.pdv.api.finance.dto.PaymentRequest;
import com.pdv.api.finance.dto.PaymentResponse;
import com.pdv.api.finance.entity.PaymentEntity;
import com.pdv.api.finance.entity.PaymentStatus;
import com.pdv.api.finance.repository.PaymentRepository;
import com.pdv.api.sale.entity.SaleEntity;
import com.pdv.api.sale.repository.SaleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SaleRepository saleRepository;

    public PaymentService(PaymentRepository paymentRepository, SaleRepository saleRepository) {
        this.paymentRepository = paymentRepository;
        this.saleRepository = saleRepository;
    }

    @Transactional
    public PaymentResponse registerPayment(PaymentRequest request) {
        SaleEntity sale = saleRepository.findById(request.saleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale not found"));

        validatePaymentAmounts(sale, request);

        PaymentEntity payment = new PaymentEntity(
                sale,
                request.paymentMethod(),
                request.grossAmount(),
                request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO,
                request.netAmount(),
                PaymentStatus.PENDING
        );

        payment.setTransactionId(request.transactionId());
        payment.setCardBrand(request.cardBrand());
        payment.setCardLastDigits(request.cardLastDigits());
        payment.setNotes(request.notes());

        PaymentEntity saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    @Transactional
    public PaymentResponse processPayment(Long paymentId, PaymentStatus newStatus, String transactionId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        if (payment.getStatus() == PaymentStatus.APPROVED || payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot change status of finalized payment");
        }

        payment.setStatus(newStatus);
        if (transactionId != null && !transactionId.isBlank()) {
            payment.setTransactionId(transactionId);
        }
        if (newStatus == PaymentStatus.APPROVED || newStatus == PaymentStatus.REJECTED) {
            payment.setProcessedAt(OffsetDateTime.now());
            if (newStatus == PaymentStatus.APPROVED) {
                payment.setPaidAmount(payment.getNetAmount());
            }
        }

        return toResponse(paymentRepository.save(payment));
    }

    @Transactional
    public PaymentResponse approvePayment(Long paymentId, String transactionId) {
        return processPayment(paymentId, PaymentStatus.APPROVED, transactionId);
    }

    @Transactional
    public PaymentResponse rejectPayment(Long paymentId, String reason) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
        payment.setStatus(PaymentStatus.REJECTED);
        payment.setProcessedAt(OffsetDateTime.now());
        if (reason != null && !reason.isBlank()) {
            payment.setNotes((payment.getNotes() != null ? payment.getNotes() + "; " : "") + "Rejection reason: " + reason);
        }
        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse getPayment(Long id) {
        return paymentRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    public List<PaymentResponse> getPaymentsBySale(Long saleId) {
        return paymentRepository.findBySaleId(saleId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void validatePaymentAmounts(SaleEntity sale, PaymentRequest request) {
        if (request.grossAmount().compareTo(sale.getTotalAmount()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Gross amount must match sale total: " + sale.getTotalAmount());
        }

        BigDecimal expectedNet = sale.getTotalAmount().subtract(sale.getDiscount());
        if (request.netAmount().compareTo(expectedNet) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Net amount must match sale total minus discount: " + expectedNet);
        }

        BigDecimal totalPaid = paymentRepository.findBySaleId(sale.getId()).stream()
                .filter(p -> p.getStatus() == PaymentStatus.APPROVED || p.getStatus() == PaymentStatus.PARTIAL)
                .map(PaymentEntity::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingAmount = sale.getTotalAmount().subtract(sale.getDiscount()).subtract(totalPaid);
        if (request.netAmount().compareTo(remainingAmount) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Payment amount exceeds remaining balance: " + remainingAmount);
        }
    }

    private PaymentResponse toResponse(PaymentEntity entity) {
        return new PaymentResponse(
                entity.getId(),
                entity.getSale().getId(),
                entity.getPaymentMethod(),
                entity.getGrossAmount(),
                entity.getDiscountAmount(),
                entity.getNetAmount(),
                entity.getPaidAmount(),
                entity.getStatus(),
                entity.getTransactionId(),
                entity.getCardBrand(),
                entity.getCardLastDigits(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getProcessedAt()
        );
    }
}