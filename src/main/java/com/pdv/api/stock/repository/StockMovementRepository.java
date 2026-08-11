package com.pdv.api.stock.repository;

import com.pdv.api.stock.entity.MovementType;
import com.pdv.api.stock.entity.StockMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovementEntity, Long> {
    List<StockMovementEntity> findByProductId(Long productId);
    List<StockMovementEntity> findByType(MovementType type);
    List<StockMovementEntity> findByProductIdAndType(Long productId, MovementType type);
}
