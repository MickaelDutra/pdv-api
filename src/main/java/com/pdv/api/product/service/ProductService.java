package com.pdv.api.product.service;

import com.pdv.api.product.dto.ProductRequest;
import com.pdv.api.product.dto.ProductResponse;
import com.pdv.api.product.entity.ProductEntity;
import com.pdv.api.product.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductRequest request) {
        ProductEntity product = fromRequest(request);
        ProductEntity saved = productRepository.save(product);
        return toResponse(saved);
    }

    public List<ProductResponse> findAll(String name, String sku, String barcode) {
        if (sku != null && !sku.isBlank()) {
            return productRepository.findBySku(sku).stream().map(this::toResponse).collect(Collectors.toList());
        }
        if (barcode != null && !barcode.isBlank()) {
            return productRepository.findByBarcode(barcode).stream().map(this::toResponse).collect(Collectors.toList());
        }
        if (name != null && !name.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(name).stream().map(this::toResponse).collect(Collectors.toList());
        }
        return productRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProductResponse findById(Long id) {
        return productRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        ProductEntity existing = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        updateEntity(existing, request);
        return toResponse(productRepository.save(existing));
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(ProductEntity entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getName(),
                entity.getSku(),
                entity.getBarcode(),
                entity.getDescription(),
                entity.getUnit(),
                entity.getQuantityPerBox(),
                entity.getWeight(),
                entity.getBrand(),
                entity.getCategory(),
                entity.getCost(),
                entity.getPrice(),
                entity.getStockQty(),
                entity.getActive(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    private ProductEntity fromRequest(ProductRequest request) {
        return new ProductEntity(
                request.name(),
                request.sku(),
                request.barcode(),
                request.description(),
                request.unit(),
                request.quantityPerBox(),
                request.weight(),
                request.brand(),
                request.category(),
                request.cost(),
                request.price(),
                request.stockQty(),
                request.active() != null ? request.active() : true,
                request.status()
        );
    }

    private void updateEntity(ProductEntity entity, ProductRequest request) {
        entity.setName(request.name());
        entity.setSku(request.sku());
        entity.setBarcode(request.barcode());
        entity.setDescription(request.description());
        entity.setUnit(request.unit());
        entity.setQuantityPerBox(request.quantityPerBox());
        entity.setWeight(request.weight());
        entity.setBrand(request.brand());
        entity.setCategory(request.category());
        entity.setCost(request.cost());
        entity.setPrice(request.price());
        entity.setStockQty(request.stockQty());
        entity.setActive(request.active() != null ? request.active() : entity.getActive());
        entity.setStatus(request.status());
    }
}
