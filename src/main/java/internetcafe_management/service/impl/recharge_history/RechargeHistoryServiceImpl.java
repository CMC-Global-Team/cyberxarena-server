package internetcafe_management.service.impl.recharge_history;

import internetcafe_management.dto.CreateRechargeHistoryRequestDTO;
import internetcafe_management.dto.RechargeHistoryDTO;
import internetcafe_management.dto.RechargeHistorySearchRequestDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.RechargeHistory;
import internetcafe_management.exception.ResourceNotFoundException;
import internetcafe_management.mapper.recharge_history.RechargeHistoryMapper;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.recharge_history.RechargeHistoryRepository;
import internetcafe_management.service.recharge_history.RechargeHistoryService;
import internetcafe_management.service.MembershipRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RechargeHistoryServiceImpl implements RechargeHistoryService {
    
    @Autowired
    private RechargeHistoryRepository rechargeHistoryRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private RechargeHistoryMapper rechargeHistoryMapper;
    
    @Autowired
    private MembershipRankService membershipRankService;
    
    @Override
    public RechargeHistoryDTO createRechargeHistory(CreateRechargeHistoryRequestDTO request) {
        // Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
        
        // Create new recharge history
        RechargeHistory rechargeHistory = new RechargeHistory();
        rechargeHistory.setCustomerId(request.getCustomerId());
        rechargeHistory.setAmount(request.getAmount());
        rechargeHistory.setRechargeDate(LocalDateTime.now());
        
        RechargeHistory savedRechargeHistory = rechargeHistoryRepository.save(rechargeHistory);
        
        // Update customer balance
        BigDecimal currentBalance = customer.getBalance() != null ? customer.getBalance() : BigDecimal.ZERO;
        customer.setBalance(currentBalance.add(request.getAmount()));
        customerRepository.save(customer);
        
        // Tự động cập nhật rank cho TẤT CẢ khách hàng khi nạp tiền
        try {
            // Force flush để đảm bảo recharge history đã được lưu
            rechargeHistoryRepository.flush();
            
            // Tính tổng số tiền nạp của khách hàng sau khi nạp thêm
            BigDecimal totalRecharge = customerRepository.getTotalRechargeAmountByCustomerId(request.getCustomerId());
            if (totalRecharge == null) {
                totalRecharge = BigDecimal.ZERO;
            }
            
            // Tính tổng thủ công để so sánh
            BigDecimal manualTotal = currentBalance.add(request.getAmount());
            
            System.out.println("🔄 Customer " + request.getCustomerId() + " total recharge after new recharge: " + totalRecharge);
            System.out.println("🔄 Manual calculation (balance + recharge): " + manualTotal);
            System.out.println("🔄 Current membership card ID: " + customer.getMembershipCardId());
            System.out.println("🔄 Recharge amount just added: " + request.getAmount());
            System.out.println("🔄 Current balance: " + currentBalance);
            
            // Use async to avoid lock timeout with current transaction
            membershipRankService.updateMembershipRank(request.getCustomerId(), totalRecharge);
            System.out.println("✅ Updated membership rank for customer " + request.getCustomerId() + 
                             " after recharge (auto-updated)");
        } catch (Exception rankError) {
            System.err.println("❌ Error updating membership rank after recharge: " + rankError.getMessage());
            rankError.printStackTrace();
        }
        
        return rechargeHistoryMapper.toDTO(savedRechargeHistory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RechargeHistoryDTO getRechargeHistoryById(Integer rechargeId) {
        RechargeHistory rechargeHistory = rechargeHistoryRepository.findById(rechargeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recharge history not found with id: " + rechargeId));
        
        return rechargeHistoryMapper.toDTO(rechargeHistory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RechargeHistoryDTO> getAllRechargeHistory(Pageable pageable) {
        Page<RechargeHistory> rechargeHistoryPage = rechargeHistoryRepository.findAll(pageable);
        return rechargeHistoryPage.map(rechargeHistoryMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RechargeHistoryDTO> getRechargeHistoryByCustomerId(Integer customerId) {
        List<RechargeHistory> rechargeHistoryList = rechargeHistoryRepository.findByCustomerIdOrderByRechargeDateDesc(customerId);
        return rechargeHistoryList.stream()
                .map(rechargeHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RechargeHistoryDTO> getRechargeHistoryByCustomerId(Integer customerId, Pageable pageable) {
        Page<RechargeHistory> rechargeHistoryPage = rechargeHistoryRepository.findByCustomerIdOrderByRechargeDateDesc(customerId, pageable);
        return rechargeHistoryPage.map(rechargeHistoryMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RechargeHistoryDTO> searchRechargeHistory(RechargeHistorySearchRequestDTO searchRequest, Pageable pageable) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        
        // Parse date strings if provided
        if (searchRequest.getStartDate() != null) {
            startDate = searchRequest.getStartDate();
        }
        if (searchRequest.getEndDate() != null) {
            endDate = searchRequest.getEndDate();
        }
        
        Page<RechargeHistory> rechargeHistoryPage = rechargeHistoryRepository.findWithFilters(
                searchRequest.getCustomerId(),
                startDate,
                endDate,
                searchRequest.getCustomerName(),
                pageable
        );
        
        return rechargeHistoryPage.map(rechargeHistoryMapper::toDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalRechargeAmountByCustomerId(Integer customerId) {
        Double totalAmount = rechargeHistoryRepository.getTotalRechargeAmountByCustomerId(customerId);
        return totalAmount != null ? totalAmount : 0.0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getTotalRechargeAmountByCustomerIdAndDateRange(Integer customerId, String startDate, String endDate) {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        
        if (startDate != null && !startDate.isEmpty()) {
            startDateTime = LocalDateTime.parse(startDate + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (endDate != null && !endDate.isEmpty()) {
            endDateTime = LocalDateTime.parse(endDate + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        
        Double totalAmount = rechargeHistoryRepository.getTotalRechargeAmountByCustomerIdAndDateRange(
                customerId, startDateTime, endDateTime);
        return totalAmount != null ? totalAmount : 0.0;
    }
    
    @Override
    public void deleteRechargeHistory(Integer rechargeId) {
        RechargeHistory rechargeHistory = rechargeHistoryRepository.findById(rechargeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recharge history not found with id: " + rechargeId));
        
        // Update customer balance (subtract the amount)
        Customer customer = customerRepository.findById(rechargeHistory.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        
        BigDecimal currentBalance = customer.getBalance() != null ? customer.getBalance() : BigDecimal.ZERO;
        customer.setBalance(currentBalance.subtract(rechargeHistory.getAmount()));
        customerRepository.save(customer);
        
        // Delete the recharge history
        rechargeHistoryRepository.deleteById(rechargeId);
        
        // Update membership rank after deletion (recalculate based on remaining recharge history)
        membershipRankService.updateMembershipRankSync(rechargeHistory.getCustomerId(), BigDecimal.ZERO);
    }
}
