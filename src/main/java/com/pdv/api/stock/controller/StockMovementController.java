package com.pdv.api.stock.controller;

import com.pdv.api.stock.dto.StockMovementRequest;
import com.pdv.api.stock.dto.StockMovementResponse;
import com.pdv.api.stock.entity.MovementType;
import com.pdv.api.stock.service.StockMovementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping("/movements")
    public ResponseEntity<StockMovementResponse> registerMovement(@Valid @RequestBody StockMovementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockMovementService.registerMovement(request));
    }

    @GetMapping("/movements")
    public ResponseEntity<List<StockMovementResponse>> listMovements(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) MovementType type) {
        return ResponseEntity.ok(stockMovementService.getMovements(productId, type));
    }
}
