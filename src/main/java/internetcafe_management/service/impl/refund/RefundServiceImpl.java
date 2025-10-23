package internetcafe_management.service.impl.refund;

import internetcafe_management.dto.RefundDTO;
import internetcafe_management.dto.RefundDetailDTO;
import internetcafe_management.entity.*;
import internetcafe_management.exception.ResourceNotFoundException;
import internetcafe_management.repository.refund.RefundRepository;
import internetcafe_management.repository.sale.SaleRepository;
import internetcafe_management.repository.product.ProductRepository;
import internetcafe_management.service.refund.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public RefundDTO createRefund(RefundDTO refundDTO) {
        try {
            // Kiểm tra sale có tồn tại không
            Sale sale = saleRepository.findById(refundDTO.getSaleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + refundDTO.getSaleId()));

            // Kiểm tra xem sale đã có refund chưa
            if (hasRefundForSale(refundDTO.getSaleId())) {
                throw new RuntimeException("Sale already has a refund");
            }

            // Tạo Refund entity
            Refund refund = new Refund();
            refund.setSale(sale);
            refund.setRefundDate(refundDTO.getRefundDate() != null ? refundDTO.getRefundDate() : LocalDateTime.now());
            refund.setRefundAmount(refundDTO.getRefundAmount());
            refund.setRefundReason(refundDTO.getRefundReason());
            refund.setRefundType(refundDTO.getRefundType());
            refund.setProcessedBy(refundDTO.getProcessedBy());
            refund.setStatus(refundDTO.getStatus() != null ? refundDTO.getStatus() : Refund.RefundStatus.Pending);

            // Tạo RefundDetail nếu có
            if (refundDTO.getRefundDetails() != null && !refundDTO.getRefundDetails().isEmpty()) {
                List<RefundDetail> refundDetails = refundDTO.getRefundDetails().stream()
                        .map(detailDTO -> {
                            RefundDetail detail = new RefundDetail();
                            detail.setRefund(refund);
                            // Tìm SaleDetail theo ID
                            SaleDetail saleDetail = sale.getSaleDetails().stream()
                                    .filter(sd -> sd.getSaleDetailId().equals(detailDTO.getSaleDetailId()))
                                    .findFirst()
                                    .orElseThrow(() -> new ResourceNotFoundException("SaleDetail not found with ID: " + detailDTO.getSaleDetailId()));
                            detail.setSaleDetail(saleDetail);
                            detail.setQuantity(detailDTO.getQuantity());
                            return detail;
                        })
                        .collect(Collectors.toList());
                refund.setRefundDetails(refundDetails);
            }

            Refund savedRefund = refundRepository.save(refund);
            return mapToDTO(savedRefund);
        } catch (Exception e) {
            log.error("Error creating refund: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create refund: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public RefundDTO updateRefund(Integer refundId, RefundDTO refundDTO) {
        try {
            Refund existingRefund = refundRepository.findById(refundId)
                    .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + refundId));

            // Cập nhật thông tin refund
            if (refundDTO.getRefundAmount() != null) {
                existingRefund.setRefundAmount(refundDTO.getRefundAmount());
            }
            if (refundDTO.getRefundReason() != null) {
                existingRefund.setRefundReason(refundDTO.getRefundReason());
            }
            if (refundDTO.getRefundType() != null) {
                existingRefund.setRefundType(refundDTO.getRefundType());
            }
            if (refundDTO.getProcessedBy() != null) {
                existingRefund.setProcessedBy(refundDTO.getProcessedBy());
            }
            if (refundDTO.getStatus() != null) {
                existingRefund.setStatus(refundDTO.getStatus());
            }

            Refund savedRefund = refundRepository.save(existingRefund);
            return mapToDTO(savedRefund);
        } catch (Exception e) {
            log.error("Error updating refund: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update refund: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteRefund(Integer refundId) {
        try {
            if (!refundRepository.existsById(refundId)) {
                throw new ResourceNotFoundException("Refund not found with ID: " + refundId);
            }
            refundRepository.deleteById(refundId);
        } catch (Exception e) {
            log.error("Error deleting refund: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete refund: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RefundDTO getRefundById(Integer refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + refundId));
        return mapToDTO(refund);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RefundDTO> getAllRefunds(Pageable pageable) {
        Page<Refund> refunds = refundRepository.findAll(pageable);
        return refunds.map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RefundDTO> searchRefunds(Refund.RefundStatus status, String processedBy, 
                                        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Page<Refund> refunds = refundRepository.findWithFilters(status, processedBy, startDate, endDate, pageable);
        return refunds.map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundDTO> getRefundsBySaleId(Integer saleId) {
        List<Refund> refunds = refundRepository.findBySaleSaleId(saleId);
        return refunds.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RefundDTO> getRefundsByCustomerId(Integer customerId, Pageable pageable) {
        Page<Refund> refunds = refundRepository.findByCustomerId(customerId, pageable);
        return refunds.map(this::mapToDTO);
    }

    @Override
    @Transactional
    public RefundDTO updateRefundStatus(Integer refundId, Refund.RefundStatus status, String processedBy) {
        try {
            Refund refund = refundRepository.findById(refundId)
                    .orElseThrow(() -> new ResourceNotFoundException("Refund not found with ID: " + refundId));

            refund.setStatus(status);
            if (processedBy != null) {
                refund.setProcessedBy(processedBy);
            }

            Refund savedRefund = refundRepository.save(refund);
            return mapToDTO(savedRefund);
        } catch (Exception e) {
            log.error("Error updating refund status: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update refund status: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getRefundCountByStatus(Refund.RefundStatus status) {
        return refundRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalRefundAmountByStatus(Refund.RefundStatus status) {
        return refundRepository.sumRefundAmountByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRefundForSale(Integer saleId) {
        return !refundRepository.findBySaleSaleId(saleId).isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RefundDTO> getPendingRefunds(Pageable pageable) {
        Page<Refund> refunds = refundRepository.findByStatus(Refund.RefundStatus.Pending, pageable);
        return refunds.map(this::mapToDTO);
    }

    private RefundDTO mapToDTO(Refund refund) {
        RefundDTO dto = new RefundDTO();
        dto.setRefundId(refund.getRefundId());
        dto.setSaleId(refund.getSale().getSaleId());
        dto.setRefundDate(refund.getRefundDate());
        dto.setRefundAmount(refund.getRefundAmount());
        dto.setRefundReason(refund.getRefundReason());
        dto.setRefundType(refund.getRefundType());
        dto.setProcessedBy(refund.getProcessedBy());
        dto.setStatus(refund.getStatus());

        // Thông tin khách hàng
        if (refund.getSale().getCustomer() != null) {
            dto.setCustomerName(refund.getSale().getCustomer().getCustomerName());
            dto.setCustomerPhone(refund.getSale().getCustomer().getPhoneNumber());
        }

        // Thông tin hóa đơn gốc
        dto.setOriginalSaleDate(refund.getSale().getSaleDate());
        if (refund.getSale().getSaleTotal() != null) {
            dto.setOriginalSaleAmount(refund.getSale().getSaleTotal().getTotalAmount());
        }

        // Chi tiết refund
        if (refund.getRefundDetails() != null && !refund.getRefundDetails().isEmpty()) {
            List<RefundDetailDTO> detailDTOs = refund.getRefundDetails().stream()
                    .map(detail -> {
                        RefundDetailDTO detailDTO = new RefundDetailDTO();
                        detailDTO.setRefundDetailId(detail.getRefundDetailId());
                        detailDTO.setRefundId(detail.getRefund().getRefundId());
                        detailDTO.setSaleDetailId(detail.getSaleDetail().getSaleDetailId());
                        detailDTO.setQuantity(detail.getQuantity());

                        // Thông tin sản phẩm - lấy từ ProductRepository
                        Integer itemId = detail.getSaleDetail().getItemId();
                        if (itemId != null) {
                            try {
                                Product product = productRepository.findById(itemId).orElse(null);
                                if (product != null) {
                                    detailDTO.setItemName(product.getItemName());
                                    detailDTO.setItemPrice(product.getPrice());
                                    detailDTO.setTotalAmount(product.getPrice()
                                            .multiply(BigDecimal.valueOf(detail.getQuantity())));
                                } else {
                                    detailDTO.setItemName("Sản phẩm #" + itemId + " (không tồn tại)");
                                    detailDTO.setItemPrice(BigDecimal.ZERO);
                                    detailDTO.setTotalAmount(BigDecimal.ZERO);
                                }
                            } catch (Exception e) {
                                log.warn("Không thể lấy thông tin sản phẩm với ID: {}", itemId, e);
                                detailDTO.setItemName("Sản phẩm #" + itemId);
                                detailDTO.setItemPrice(BigDecimal.ZERO);
                                detailDTO.setTotalAmount(BigDecimal.ZERO);
                            }
                        }
                        return detailDTO;
                    })
                    .collect(Collectors.toList());
            dto.setRefundDetails(detailDTOs);
        }

        return dto;
    }
}
