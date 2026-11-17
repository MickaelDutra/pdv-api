package com.pdv.api.finance.repository;

import com.pdv.api.finance.entity.PaymentEntity;
import com.pdv.api.finance.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findBySaleId(Long saleId);

    Optional<PaymentEntity> findBySaleIdAndPaymentMethod(Long saleId, com.pdv.api.finance.entity.PaymentMethod paymentMethod);

    List<PaymentEntity> findByStatus(PaymentStatus status);

    List<PaymentEntity> findByTransactionId(String transactionId);
}