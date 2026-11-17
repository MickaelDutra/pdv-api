package com.pdv.api.sale.repository;

import com.pdv.api.sale.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);
}
