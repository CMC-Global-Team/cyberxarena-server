package internetcafe_management.service.impl.revenue;

import internetcafe_management.dto.RevenueDTO;
import internetcafe_management.entity.Revenue;
import internetcafe_management.mapper.revenue.RevenueMapper;
import internetcafe_management.repository.revenue.RevenueRepository;
import internetcafe_management.repository.sale.SaleRepository;
import internetcafe_management.repository.session.SessionRepository;
import internetcafe_management.service.revenue.RevenueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RevenueServiceImpl implements RevenueService {

    private final RevenueRepository revenueRepository;
    private final RevenueMapper revenueMapper;
    private final SessionRepository sessionRepository; // Thêm
    private final SaleRepository saleRepository;

    public RevenueServiceImpl(RevenueRepository revenueRepository, RevenueMapper revenueMapper,
                                SessionRepository sessionRepo, SaleRepository saleRepo) {
        this.revenueRepository = revenueRepository;
        this.revenueMapper = revenueMapper;
        this.sessionRepository = sessionRepo;
        this.saleRepository = saleRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RevenueDTO> getAllRevenue(Pageable pageable) {
        // 1. Lấy Page<Revenue> từ DB
        Page<Revenue> revenuePage = revenueRepository.findAll(pageable);

        // 2. Dùng mapper để chuyển đổi Page<Revenue> thành Page<RevenueDTO>
        // Mapper sẽ tự động tính totalRevenue cho chúng ta
        return revenuePage.map(revenueMapper::toDto);
    }

    @Override
    public List<RevenueDTO> generateRevenueReports(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu không thể sau ngày kết thúc.");
        }

        List<RevenueDTO> generatedReports = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (revenueRepository.existsByDate(date)) {
                continue;
            }

            //tính tiền máy
            BigDecimal computerTotal = Optional.ofNullable(sessionRepository.sumTotalAmountByEndDateTime(date))
                    .orElse(BigDecimal.ZERO);

            //tính tiền bán hàng
            BigDecimal salesTotal = Optional.ofNullable(saleRepository.sumTotalAmountBySaleDate(date))
                    .orElse(BigDecimal.ZERO);

            //tạo báo cáo mới
            Revenue newReport = new Revenue();
            newReport.setDate(date.atStartOfDay());
            newReport.setComputerUsageRevenue(computerTotal);
            newReport.setSalesRevenue(salesTotal);

            //lưu vào db
            Revenue savedReport = revenueRepository.save(newReport);
            generatedReports.add(revenueMapper.toDto(savedReport));
        }

        return generatedReports;
    }

    @Override
    public RevenueDTO recalculateRevenueReport(LocalDate date) {
        // Phải tìm báo cáo đã tồn tại, nếu không thì báo lỗi
        Revenue existingReport = revenueRepository.findByDate(date)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy báo cáo doanh thu cho ngày " + date));

        BigDecimal computerTotal = Optional.ofNullable(sessionRepository.sumTotalAmountByEndDateTime(date))
                .orElse(BigDecimal.ZERO);

        BigDecimal salesTotal = Optional.ofNullable(saleRepository.sumTotalAmountBySaleDate(date))
                .orElse(BigDecimal.ZERO);

        existingReport.setComputerUsageRevenue(computerTotal);
        existingReport.setSalesRevenue(salesTotal);

        Revenue updatedReport = revenueRepository.save(existingReport);

        return revenueMapper.toDto(updatedReport);
    }
}