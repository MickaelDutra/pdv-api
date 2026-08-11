package com.pdv.api.stock.entity;

import com.pdv.api.product.entity.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "stock_movement")
public class StockMovementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MovementType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String createdBy;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public StockMovementEntity() {
    }

    public StockMovementEntity(ProductEntity product, MovementType type, Integer quantity, String createdBy, String notes) {
        this.product = product;
        this.type = type;
        this.quantity = quantity;
        this.createdBy = createdBy;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public MovementType getType() {
        return type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getNotes() {
        return notes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
