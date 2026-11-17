package com.pdv.api.finance.controller;

import com.pdv.api.finance.dto.PaymentRequest;
import com.pdv.api.finance.dto.PaymentResponse;
import com.pdv.api.finance.entity.PaymentStatus;
import com.pdv.api.finance.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> registerPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.registerPayment(request));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> listPayments(
            @RequestParam(required = false) Long saleId,
            @RequestParam(required = false) PaymentStatus status) {
        if (saleId != null) {
            return ResponseEntity.ok(paymentService.getPaymentsBySale(saleId));
        }
        if (status != null) {
            return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
        }
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PaymentResponse> approvePayment(
            @PathVariable Long id,
            @RequestParam(required = false) String transactionId) {
        return ResponseEntity.ok(paymentService.approvePayment(id, transactionId));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<PaymentResponse> rejectPayment(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(paymentService.rejectPayment(id, reason));
    }
}