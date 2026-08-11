package com.pdv.api.sale.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale")
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal discount;

    @Column(nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItemEntity> items = new ArrayList<>();

    public SaleEntity() {
    }

    public SaleEntity(String customerName, String paymentMethod, String createdBy, BigDecimal totalAmount, BigDecimal discount, String status) {
        this.customerName = customerName;
        this.paymentMethod = paymentMethod;
        this.createdBy = createdBy;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.status = status;
    }

    public void addItem(SaleItemEntity item) {
        items.add(item);
        item.setSale(this);
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public List<SaleItemEntity> getItems() {
        return items;
    }
}
