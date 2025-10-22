package internetcafe_management.service.recharge_history;

import internetcafe_management.dto.CreateRechargeHistoryRequestDTO;
import internetcafe_management.dto.RechargeHistoryDTO;
import internetcafe_management.dto.RechargeHistorySearchRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RechargeHistoryService {
    
    /**
     * Create a new recharge history record
     */
    RechargeHistoryDTO createRechargeHistory(CreateRechargeHistoryRequestDTO request);
    
    /**
     * Get recharge history by ID
     */
    RechargeHistoryDTO getRechargeHistoryById(Integer rechargeId);
    
    /**
     * Get all recharge history with pagination
     */
    Page<RechargeHistoryDTO> getAllRechargeHistory(Pageable pageable);
    
    /**
     * Get recharge history by customer ID
     */
    List<RechargeHistoryDTO> getRechargeHistoryByCustomerId(Integer customerId);
    
    /**
     * Get recharge history by customer ID with pagination
     */
    Page<RechargeHistoryDTO> getRechargeHistoryByCustomerId(Integer customerId, Pageable pageable);
    
    /**
     * Search recharge history with filters
     */
    Page<RechargeHistoryDTO> searchRechargeHistory(RechargeHistorySearchRequestDTO searchRequest, Pageable pageable);
    
    /**
     * Get total recharge amount for a customer
     */
    Double getTotalRechargeAmountByCustomerId(Integer customerId);
    
    /**
     * Get total recharge amount for a customer in date range
     */
    Double getTotalRechargeAmountByCustomerIdAndDateRange(Integer customerId, 
                                                         String startDate, 
                                                         String endDate);
    
    /**
     * Delete recharge history by ID
     */
    void deleteRechargeHistory(Integer rechargeId);
}
