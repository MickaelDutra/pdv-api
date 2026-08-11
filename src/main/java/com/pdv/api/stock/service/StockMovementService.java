package com.pdv.api.stock.service;

import com.pdv.api.product.entity.ProductEntity;
import com.pdv.api.product.repository.ProductRepository;
import com.pdv.api.stock.dto.StockMovementRequest;
import com.pdv.api.stock.dto.StockMovementResponse;
import com.pdv.api.stock.entity.MovementType;
import com.pdv.api.stock.entity.StockMovementEntity;
import com.pdv.api.stock.repository.StockMovementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;

    public StockMovementService(StockMovementRepository stockMovementRepository, ProductRepository productRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public StockMovementResponse registerMovement(StockMovementRequest request) {
        ProductEntity product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (request.quantity() == null || request.quantity() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be non-zero");
        }

        if (request.type() != MovementType.AJUSTE && request.quantity() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive for this movement type");
        }

        if (request.type() == MovementType.AJUSTE && product.getStockQty() + request.quantity() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Adjustment cannot result in negative stock");
        }

        if ((request.type() == MovementType.SAIDA || request.type() == MovementType.VENDA) && product.getStockQty() < request.quantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for movement");
        }

        int newQuantity = calculateNewStock(product.getStockQty(), request.type(), request.quantity());
        product.setStockQty(newQuantity);
        productRepository.save(product);

        StockMovementEntity movement = new StockMovementEntity(
                product,
                request.type(),
                request.quantity(),
                request.createdBy(),
                request.notes()
        );

        return toResponse(stockMovementRepository.save(movement));
    }

    public List<StockMovementResponse> getMovements(Long productId, MovementType type) {
        return selectMovements(productId, type).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private List<StockMovementEntity> selectMovements(Long productId, MovementType type) {
        if (productId != null && type != null) {
            return stockMovementRepository.findByProductIdAndType(productId, type);
        }
        if (productId != null) {
            return stockMovementRepository.findByProductId(productId);
        }
        if (type != null) {
            return stockMovementRepository.findByType(type);
        }
        return stockMovementRepository.findAll();
    }

    private int calculateNewStock(Integer currentStock, MovementType type, Integer quantity) {
        return switch (type) {
            case ENTRADA, DEVOLUCAO -> currentStock + quantity;
            case SAIDA, VENDA -> currentStock - quantity;
            case AJUSTE -> currentStock + quantity;
        };
    }

    private StockMovementResponse toResponse(StockMovementEntity entity) {
        return new StockMovementResponse(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getProduct().getName(),
                entity.getType(),
                entity.getQuantity(),
                entity.getCreatedBy(),
                entity.getNotes(),
                entity.getCreatedAt()
        );
    }
}
