package com.pdv.api.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 100)
    private String sku;

    @Column(length = 100)
    private String barcode;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String unit;

    @Column(name = "quantity_per_box")
    private Integer quantityPerBox;

    private BigDecimal weight;

    private String brand;

    private String category;

    private BigDecimal cost;

    private BigDecimal price;

    @Column(name = "stock_qty", nullable = false)
    private Integer stockQty = 0;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "status", length = 50)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public ProductEntity() {
    }

    public ProductEntity(String name, String sku, String barcode, String description, String unit,
                         Integer quantityPerBox, BigDecimal weight, String brand, String category,
                         BigDecimal cost, BigDecimal price, Integer stockQty, Boolean active, String status) {
        this.name = name;
        this.sku = sku;
        this.barcode = barcode;
        this.description = description;
        this.unit = unit;
        this.quantityPerBox = quantityPerBox;
        this.weight = weight;
        this.brand = brand;
        this.category = category;
        this.cost = cost;
        this.price = price;
        this.stockQty = stockQty;
        this.active = active;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }

    public Integer getQuantityPerBox() {
        return quantityPerBox;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQty() {
        return stockQty;
    }

    public Boolean getActive() {
        return active;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setQuantityPerBox(Integer quantityPerBox) {
        this.quantityPerBox = quantityPerBox;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
