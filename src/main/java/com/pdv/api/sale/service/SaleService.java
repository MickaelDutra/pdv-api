package com.pdv.api.sale.service;

import com.pdv.api.product.entity.ProductEntity;
import com.pdv.api.product.repository.ProductRepository;
import com.pdv.api.sale.dto.SaleItemRequest;
import com.pdv.api.sale.dto.SaleItemResponse;
import com.pdv.api.sale.dto.SaleRequest;
import com.pdv.api.sale.dto.SaleResponse;
import com.pdv.api.sale.entity.SaleEntity;
import com.pdv.api.sale.entity.SaleItemEntity;
import com.pdv.api.sale.repository.SaleRepository;
import com.pdv.api.stock.entity.MovementType;
import com.pdv.api.stock.service.StockMovementService;
import com.pdv.api.stock.dto.StockMovementRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final StockMovementService stockMovementService;

    public SaleService(SaleRepository saleRepository, ProductRepository productRepository, StockMovementService stockMovementService) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.stockMovementService = stockMovementService;
    }

    @Transactional
    public SaleResponse createSale(SaleRequest request) {
        if (request.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one sale item is required");
        }

        List<SaleItemEntity> items = request.items().stream()
                .map(this::buildSaleItem)
                .collect(Collectors.toList());

        BigDecimal itemsTotal = items.stream()
                .map(SaleItemEntity::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmount = itemsTotal.subtract(request.discount() != null ? request.discount() : BigDecimal.ZERO);
        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sale total cannot be negative");
        }

        SaleEntity sale = new SaleEntity(
                request.customerName(),
                request.paymentMethod(),
                request.createdBy(),
                totalAmount,
                request.discount() != null ? request.discount() : BigDecimal.ZERO,
                "COMPLETED"
        );

        items.forEach(sale::addItem);
        SaleEntity savedSale = saleRepository.save(sale);
        items.forEach(item -> registerStockMovement(item, request.createdBy()));

        return toResponse(savedSale);
    }

    public List<SaleResponse> listSales() {
        return saleRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public SaleResponse getSale(Long id) {
        return saleRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale not found"));
    }

    private SaleItemEntity buildSaleItem(SaleItemRequest itemRequest) {
        ProductEntity product = productRepository.findById(itemRequest.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + itemRequest.productId()));

        if (product.getStockQty() < itemRequest.quantity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for product: " + product.getName());
        }

        BigDecimal discount = itemRequest.discount() != null ? itemRequest.discount() : BigDecimal.ZERO;
        BigDecimal total = itemRequest.unitPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())).subtract(discount);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item total cannot be negative");
        }

        return new SaleItemEntity(product, itemRequest.quantity(), itemRequest.unitPrice(), total, discount);
    }

    private void registerStockMovement(SaleItemEntity item, String createdBy) {
        StockMovementRequest movementRequest = new StockMovementRequest(
                item.getProduct().getId(),
                MovementType.VENDA,
                item.getQuantity(),
                createdBy,
                "Venda registrada"
        );
        stockMovementService.registerMovement(movementRequest);
    }

    private SaleResponse toResponse(SaleEntity entity) {
        List<SaleItemResponse> items = entity.getItems().stream()
                .map(item -> new SaleItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotal(),
                        item.getDiscount()))
                .collect(Collectors.toList());

        return new SaleResponse(
                entity.getId(),
                entity.getCustomerName(),
                entity.getPaymentMethod(),
                entity.getCreatedBy(),
                entity.getTotalAmount(),
                entity.getDiscount(),
                entity.getStatus(),
                entity.getCreatedAt(),
                items
        );
    }
}
