package internetcafe_management.service.impl.revenue;

import internetcafe_management.dto.RevenueDTO;
import internetcafe_management.entity.Revenue;
import internetcafe_management.entity.Sale;
import internetcafe_management.mapper.revenue.RevenueMapper;
import internetcafe_management.repository.revenue.RevenueRepository;
import internetcafe_management.repository.sale.SaleRepository;
import internetcafe_management.repository.session.SessionRepository;
import internetcafe_management.repository.recharge_history.RechargeHistoryRepository;
import internetcafe_management.service.revenue.RevenueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class RevenueServiceImpl implements RevenueService {

    private final RevenueRepository revenueRepository;
    private final RevenueMapper revenueMapper;
    private final SessionRepository sessionRepository;
    private final SaleRepository saleRepository;
    private final RechargeHistoryRepository rechargeHistoryRepository;

    public RevenueServiceImpl(RevenueRepository revenueRepository, RevenueMapper revenueMapper,
                                SessionRepository sessionRepo, SaleRepository saleRepo,
                                RechargeHistoryRepository rechargeHistoryRepository) {
        this.revenueRepository = revenueRepository;
        this.revenueMapper = revenueMapper;
        this.sessionRepository = sessionRepo;
        this.saleRepository = saleRepo;
        this.rechargeHistoryRepository = rechargeHistoryRepository;
        log.info("🚀 RevenueServiceImpl initialized successfully!");
        System.out.println("🚀 RevenueServiceImpl initialized successfully!");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RevenueDTO> getAllRevenue(Pageable pageable) {
        try {
            log.info("Getting all revenue with pageable: {}", pageable);
            
            // 1. Lấy Page<Revenue> từ DB
            Page<Revenue> revenuePage = revenueRepository.findAll(pageable);
            log.info("Found {} revenue records from database", revenuePage.getTotalElements());

            // 2. Dùng mapper để chuyển đổi Page<Revenue> thành Page<RevenueDTO>
            // Mapper sẽ tự động tính totalRevenue cho chúng ta
            Page<RevenueDTO> result = revenuePage.map(revenueMapper::toDto);
            log.info("Successfully mapped {} revenue records to DTOs", result.getTotalElements());
            
            return result;
        } catch (Exception e) {
            log.error("Error getting all revenue: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get revenue data: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RevenueDTO> generateRevenueReports(LocalDate startDate, LocalDate endDate) {
        log.info("Generating revenue reports from {} to {}", startDate, endDate);
        
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu không thể sau ngày kết thúc.");
        }

        List<RevenueDTO> generatedReports = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            try {
                LocalDateTime dateTime = date.atStartOfDay();
                
                // Always recalculate, don't skip existing reports
                log.info("Processing revenue report for date: {}", date);

                // Tính tiền máy - Lấy từ lịch sử nạp tiền (recharge history)
                BigDecimal computerTotal;
                try {
                    BigDecimal rechargeAmount = rechargeHistoryRepository.sumRechargeAmountByDate(date);
                    log.info("🔍 Recharge amount for {}: {}", date, rechargeAmount);
                    computerTotal = Optional.ofNullable(rechargeAmount)
                            .orElse(BigDecimal.ZERO);
                    log.info("💰 Computer usage revenue (from recharges) for {}: {}", date, computerTotal);
                    
                } catch (Exception e) {
                    log.error("❌ Error calculating computer revenue for date {}: {}", date, e.getMessage(), e);
                    computerTotal = BigDecimal.ZERO;
                }

                // Tính tiền bán hàng (trừ refunds đã approve)
                BigDecimal salesTotal;
                try {
                    // Try direct calculation first
                    BigDecimal grossSales = saleRepository.calculateSalesRevenueByDate(date);
                    log.info("🔍 Sales revenue (direct calculation) raw query result for {}: {}", date, grossSales);
                    grossSales = Optional.ofNullable(grossSales)
                            .orElse(BigDecimal.ZERO);
                    
                    // Trừ refunds đã approve cho ngày này
                    BigDecimal refundsTotal = saleRepository.sumRefundedAmountBySaleDate(date);
                    log.info("🔍 Refunds raw query result for {}: {}", date, refundsTotal);
                    refundsTotal = Optional.ofNullable(refundsTotal)
                            .orElse(BigDecimal.ZERO);
                    
                    salesTotal = grossSales.subtract(refundsTotal);
                    log.info("💰 Sales revenue for {}: Gross={}, Refunds={}, Net={}", date, grossSales, refundsTotal, salesTotal);
                    
                    // Debug: Check if there are any sales on this date
                    final LocalDate filterDateForSales = date; // Make effectively final for lambda
                    long saleCount = saleRepository.findAll().stream()
                            .filter(s -> s.getSaleDate() != null && 
                                       s.getSaleDate().toLocalDate().equals(filterDateForSales))
                            .count();
                    log.info("📊 Sales on {}: {}", date, saleCount);
                    
                    // Debug: Check all sales to see their saleDate
                    List<Sale> allSales = saleRepository.findAll();
                    log.info("🔍 All sales count: {}", allSales.size());
                    for (Sale sale : allSales) {
                        log.info("Sale {}: saleDate={}, status={}", 
                                sale.getSaleId(), 
                                sale.getSaleDate(), 
                                sale.getStatus());
                    }
                    
                } catch (Exception e) {
                    log.error("❌ Error calculating sales revenue for date {}: {}", date, e.getMessage(), e);
                    salesTotal = BigDecimal.ZERO;
                }

                // Tạo hoặc cập nhật báo cáo
                Revenue report = revenueRepository.findByDate(dateTime)
                        .orElse(new Revenue());
                
                report.setDate(dateTime);
                report.setComputerUsageRevenue(computerTotal);
                report.setSalesRevenue(salesTotal);

                // Lưu vào db
                Revenue savedReport = revenueRepository.save(report);
                
                // Map to DTO với totalRevenue
                RevenueDTO dto = revenueMapper.toDto(savedReport);
                
                log.info("✅ Generated/Updated revenue report for {}: Computer={}, Sales={}, Total={}", 
                        date, computerTotal, salesTotal, dto.getTotalRevenue());
                
                generatedReports.add(dto);
                        
            } catch (Exception e) {
                log.error("Error generating revenue report for date {}: {}", date, e.getMessage(), e);
                // Continue with next date instead of failing completely
            }
        }

        log.info("Generated {} revenue reports", generatedReports.size());
        return generatedReports;
    }

    @Override
    public RevenueDTO recalculateRevenueReport(LocalDate date) {
        log.info("🔄 Recalculating revenue report for date: {}", date);
        
        // Phải tìm báo cáo đã tồn tại, nếu không thì báo lỗi
        LocalDateTime dateTime = date.atStartOfDay();
        Revenue existingReport = revenueRepository.findByDate(dateTime)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy báo cáo doanh thu cho ngày " + date));

        // Tính tiền máy - Lấy từ lịch sử nạp tiền (recharge history)
        BigDecimal computerTotal;
        try {
            BigDecimal rechargeAmount = rechargeHistoryRepository.sumRechargeAmountByDate(date);
            log.info("🔍 Recharge amount for {}: {}", date, rechargeAmount);
            computerTotal = Optional.ofNullable(rechargeAmount)
                    .orElse(BigDecimal.ZERO);
            log.info("💰 Computer usage revenue (from recharges) for {}: {}", date, computerTotal);
            
        } catch (Exception e) {
            log.error("❌ Error calculating computer revenue for date {}: {}", date, e.getMessage(), e);
            computerTotal = BigDecimal.ZERO;
        }

        // Tính tiền bán hàng (trừ refunds đã approve)
        BigDecimal salesTotal;
        try {
            BigDecimal grossSales = saleRepository.calculateSalesRevenueByDate(date);
            log.info("🔍 Sales revenue (direct calculation) raw query result for {}: {}", date, grossSales);
            
            BigDecimal refundsTotal = Optional.ofNullable(saleRepository.sumRefundedAmountBySaleDate(date))
                    .orElse(BigDecimal.ZERO);
            
            salesTotal = grossSales.subtract(refundsTotal);
            log.info("💰 Sales revenue for {}: Gross={}, Refunds={}, Net={}", date, grossSales, refundsTotal, salesTotal);
            
        } catch (Exception e) {
            log.error("❌ Error calculating sales revenue for date {}: {}", date, e.getMessage(), e);
            salesTotal = BigDecimal.ZERO;
        }

        existingReport.setComputerUsageRevenue(computerTotal);
        existingReport.setSalesRevenue(salesTotal);

        Revenue updatedReport = revenueRepository.save(existingReport);
        log.info("✅ Successfully recalculated revenue report for {}: Computer={}, Sales={}", 
                date, computerTotal, salesTotal);

        return revenueMapper.toDto(updatedReport);
    }
}