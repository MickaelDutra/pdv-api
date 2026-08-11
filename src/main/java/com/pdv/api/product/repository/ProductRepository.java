package com.pdv.api.product.repository;

import com.pdv.api.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByNameContainingIgnoreCase(String name);
    List<ProductEntity> findBySku(String sku);
    List<ProductEntity> findByBarcode(String barcode);
}
