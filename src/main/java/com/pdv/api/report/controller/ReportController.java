package com.pdv.api.report.controller;

import com.pdv.api.report.dto.DailySalesReportResponse;
import com.pdv.api.report.dto.LowStockReportResponse;
import com.pdv.api.report.dto.NoMovementReportResponse;
import com.pdv.api.report.dto.ProductsSoldReportResponse;
import com.pdv.api.report.dto.RevenueReportResponse;
import com.pdv.api.report.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/daily-sales")
    public ResponseEntity<DailySalesReportResponse> getDailySalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.getDailySalesReport(date));
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueReportResponse> getRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getRevenueReport(startDate, endDate));
    }

    @GetMapping("/products-sold")
    public ResponseEntity<ProductsSoldReportResponse> getProductsSoldReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.getProductsSoldReport(startDate, endDate));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<LowStockReportResponse> getLowStockReport(
            @RequestParam(required = false) Integer threshold) {
        return ResponseEntity.ok(reportService.getLowStockReport(threshold));
    }

    @GetMapping("/no-movement")
    public ResponseEntity<NoMovementReportResponse> getNoMovementReport(
            @RequestParam(required = false) Integer daysWithoutMovement) {
        return ResponseEntity.ok(reportService.getNoMovementReport(daysWithoutMovement));
    }
}