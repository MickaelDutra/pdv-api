package com.pdv.api.report.service;

import com.pdv.api.product.entity.ProductEntity;
import com.pdv.api.product.repository.ProductRepository;
import com.pdv.api.report.dto.DailySalesReportResponse;
import com.pdv.api.report.dto.LowStockReportResponse;
import com.pdv.api.report.dto.NoMovementReportResponse;
import com.pdv.api.report.dto.ProductsSoldReportResponse;
import com.pdv.api.report.dto.RevenueReportResponse;
import com.pdv.api.sale.entity.SaleEntity;
import com.pdv.api.sale.entity.SaleItemEntity;
import com.pdv.api.sale.repository.SaleRepository;
import com.pdv.api.stock.entity.MovementType;
import com.pdv.api.stock.entity.StockMovementEntity;
import com.pdv.api.stock.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public ReportService(SaleRepository saleRepository,
                         ProductRepository productRepository,
                         StockMovementRepository stockMovementRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Transactional(readOnly = true)
    public DailySalesReportResponse getDailySalesReport(LocalDate date) {
        OffsetDateTime startOfDay = date.atStartOfDay().atOffset(ZoneOffset.of("-03:00"));
        OffsetDateTime endOfDay = date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.of("-03:00"));

        List<SaleEntity> sales = saleRepository.findByCreatedAtBetween(startOfDay, endOfDay);

        Long totalSales = (long) sales.size();
        BigDecimal totalRevenue = sales.stream()
                .map(SaleEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDiscounts = sales.stream()
                .map(SaleEntity::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netRevenue = totalRevenue.subtract(totalDiscounts);
        Long totalItemsSold = sales.stream()
                .flatMap(s -> s.getItems().stream())
                .map(SaleItemEntity::getQuantity)
                .mapToLong(Integer::longValue)
                .sum();

        List<DailySalesReportResponse.SaleSummary> saleSummaries = sales.stream()
                .map(s -> new DailySalesReportResponse.SaleSummary(
                        s.getId(),
                        s.getCustomerName(),
                        s.getPaymentMethod(),
                        s.getCreatedBy(),
                        s.getTotalAmount(),
                        s.getDiscount(),
                        s.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new DailySalesReportResponse(
                date,
                totalSales,
                totalRevenue,
                totalDiscounts,
                netRevenue,
                totalItemsSold,
                saleSummaries
        );
    }

    @Transactional(readOnly = true)
    public RevenueReportResponse getRevenueReport(LocalDate startDate, LocalDate endDate) {
        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.of("-03:00"));
        OffsetDateTime end = endDate.plusDays(1).atStartOfDay().atOffset(ZoneOffset.of("-03:00"));

        List<SaleEntity> sales = saleRepository.findByCreatedAtBetween(start, end);

        BigDecimal totalGrossRevenue = sales.stream()
                .map(s -> s.getTotalAmount().add(s.getDiscount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDiscounts = sales.stream()
                .map(SaleEntity::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalNetRevenue = sales.stream()
                .map(SaleEntity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Long totalSales = (long) sales.size();
        Long totalItemsSold = sales.stream()
                .flatMap(s -> s.getItems().stream())
                .map(SaleItemEntity::getQuantity)
                .mapToLong(Integer::longValue)
                .sum();
        BigDecimal averageTicket = totalSales > 0
                ? totalNetRevenue.divide(BigDecimal.valueOf(totalSales), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        Map<LocalDate, List<SaleEntity>> salesByDate = sales.stream()
                .collect(Collectors.groupingBy(s -> s.getCreatedAt().withOffsetSameInstant(ZoneOffset.of("-03:00")).toLocalDate()));

        List<RevenueReportResponse.DailyRevenue> dailyBreakdown = salesByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate d = entry.getKey();
                    List<SaleEntity> daySales = entry.getValue();
                    BigDecimal gross = daySales.stream().map(s -> s.getTotalAmount().add(s.getDiscount())).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal disc = daySales.stream().map(SaleEntity::getDiscount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal net = daySales.stream().map(SaleEntity::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new RevenueReportResponse.DailyRevenue(d, gross, disc, net, (long) daySales.size());
                })
                .sorted(Comparator.comparing(RevenueReportResponse.DailyRevenue::date))
                .collect(Collectors.toList());

        return new RevenueReportResponse(
                startDate,
                endDate,
                totalGrossRevenue,
                totalDiscounts,
                totalNetRevenue,
                totalSales,
                totalItemsSold,
                averageTicket,
                dailyBreakdown
        );
    }

    @Transactional(readOnly = true)
    public ProductsSoldReportResponse getProductsSoldReport(LocalDate startDate, LocalDate endDate) {
        OffsetDateTime start = startDate.atStartOfDay().atOffset(ZoneOffset.of("-03:00"));
        OffsetDateTime end = endDate.plusDays(1).atStartOfDay().atOffset(ZoneOffset.of("-03:00"));

        List<SaleEntity> sales = saleRepository.findByCreatedAtBetween(start, end);

        Map<Long, ProductsSoldReportResponse.ProductSalesSummary> productMap = new java.util.HashMap<>();

        for (SaleEntity sale : sales) {
            for (SaleItemEntity item : sale.getItems()) {
                Long productId = item.getProduct().getId();
                ProductsSoldReportResponse.ProductSalesSummary existing = productMap.get(productId);

                Long newQty = item.getQuantity() + (existing != null ? existing.quantitySold() : 0L);
                BigDecimal newRevenue = item.getTotal().add(existing != null ? existing.totalRevenue() : BigDecimal.ZERO);
                Long newSalesCount = 1L + (existing != null ? existing.numberOfSales() : 0L);

                productMap.put(productId, new ProductsSoldReportResponse.ProductSalesSummary(
                        productId,
                        item.getProduct().getName(),
                        item.getProduct().getSku(),
                        item.getProduct().getCategory(),
                        newQty,
                        newRevenue,
                        newQty > 0 ? newRevenue.divide(BigDecimal.valueOf(newQty), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO,
                        newSalesCount
                ));
            }
        }

        Long totalItemsSold = productMap.values().stream().mapToLong(ProductsSoldReportResponse.ProductSalesSummary::quantitySold).sum();
        BigDecimal totalRevenue = productMap.values().stream().map(ProductsSoldReportResponse.ProductSalesSummary::totalRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ProductsSoldReportResponse.ProductSalesSummary> sortedProducts = productMap.values().stream()
                .sorted(Comparator.comparing(ProductsSoldReportResponse.ProductSalesSummary::quantitySold).reversed())
                .collect(Collectors.toList());

        return new ProductsSoldReportResponse(
                startDate,
                endDate,
                totalItemsSold,
                totalRevenue,
                sortedProducts
        );
    }

    @Transactional(readOnly = true)
    public LowStockReportResponse getLowStockReport(Integer threshold) {
        Integer effectiveThreshold = threshold != null ? threshold : 10;

        List<ProductEntity> products = productRepository.findAll();

        List<LowStockReportResponse.LowStockProduct> lowStockProducts = products.stream()
                .filter(p -> p.getStockQty() <= effectiveThreshold && p.getActive())
                .map(p -> new LowStockReportResponse.LowStockProduct(
                        p.getId(),
                        p.getName(),
                        p.getSku(),
                        p.getCategory(),
                        p.getStockQty(),
                        effectiveThreshold,
                        p.getPrice(),
                        p.getPrice().multiply(BigDecimal.valueOf(p.getStockQty())),
                        p.getStockQty() == 0 ? "OUT_OF_STOCK" : "LOW_STOCK"
                ))
                .sorted(Comparator.comparing(LowStockReportResponse.LowStockProduct::currentStock))
                .collect(Collectors.toList());

        return new LowStockReportResponse(
                effectiveThreshold,
                (long) products.size(),
                (long) lowStockProducts.size(),
                lowStockProducts
        );
    }

    @Transactional(readOnly = true)
    public NoMovementReportResponse getNoMovementReport(Integer daysWithoutMovement) {
        Integer effectiveDays = daysWithoutMovement != null ? daysWithoutMovement : 30;
        OffsetDateTime cutoffDate = OffsetDateTime.now().minusDays(effectiveDays);

        List<ProductEntity> allProducts = productRepository.findAll();
        List<StockMovementEntity> recentMovements = stockMovementRepository.findByCreatedAtAfter(cutoffDate);

        java.util.Set<Long> movedProductIds = recentMovements.stream()
                .map(m -> m.getProduct().getId())
                .collect(Collectors.toSet());

        List<NoMovementReportResponse.NoMovementProduct> noMovementProducts = allProducts.stream()
                .filter(p -> !movedProductIds.contains(p.getId()) && p.getActive())
                .map(p -> {
                    List<StockMovementEntity> productMovements = stockMovementRepository.findByProductId(p.getId());
                    OffsetDateTime lastMovement = productMovements.isEmpty() ? p.getCreatedAt() :
                            productMovements.stream()
                                    .max(Comparator.comparing(StockMovementEntity::getCreatedAt))
                                    .map(StockMovementEntity::getCreatedAt)
                                    .orElse(p.getCreatedAt());

                    long daysSinceLastMovement = java.time.Duration.between(lastMovement, OffsetDateTime.now()).toDays();

                    return new NoMovementReportResponse.NoMovementProduct(
                            p.getId(),
                            p.getName(),
                            p.getSku(),
                            p.getCategory(),
                            p.getStockQty(),
                            p.getPrice(),
                            p.getPrice().multiply(BigDecimal.valueOf(p.getStockQty())),
                            lastMovement,
                            (int) daysSinceLastMovement,
                            p.getStockQty() == 0 ? "OUT_OF_STOCK" : "NO_MOVEMENT"
                    );
                })
                .sorted(Comparator.comparing(NoMovementReportResponse.NoMovementProduct::daysSinceLastMovement).reversed())
                .collect(Collectors.toList());

        return new NoMovementReportResponse(
                OffsetDateTime.now().minusDays(effectiveDays).toLocalDate(),
                effectiveDays,
                (long) allProducts.size(),
                (long) noMovementProducts.size(),
                noMovementProducts
        );
    }
}