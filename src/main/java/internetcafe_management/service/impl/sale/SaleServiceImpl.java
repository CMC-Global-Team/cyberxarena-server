package internetcafe_management.service.impl.sale;

import internetcafe_management.dto.SaleDTO;
import internetcafe_management.dto.UpdateSaleRequestDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.Sale;
import internetcafe_management.entity.SaleDetail;
import internetcafe_management.entity.SaleTotal;
import internetcafe_management.mapper.Customer.CustomerMapper;
import internetcafe_management.mapper.sale.SaleMapper;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.discount.DiscountRepository;
import internetcafe_management.repository.sale.SaleRepository;
import internetcafe_management.service.Customer.CustomerService;
import internetcafe_management.service.sale.SaleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SaleServiceImpl implements SaleService {
    private final SaleMapper saleMapper;
    private final SaleRepository saleRepository;
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final DiscountRepository discountRepository;
    @Override
    @Transactional
    public SaleDTO create(SaleDTO dto, Integer customerId) {
        try {
            // Get customer
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
            
            // Create sale entity without SaleTotal first
            Sale entity = new Sale();
            entity.setCustomer(customer);
            entity.setSaleDate(dto.getSaleDate());
            entity.setDiscountId(dto.getDiscountId() != null ? dto.getDiscountId() : 1);
            entity.setPaymentMethod(dto.getPaymentMethod());
            entity.setNote(dto.getNote());
            
            // Map SaleDetailDTO to SaleDetail - handle null items
            if (dto.getItems() != null && !dto.getItems().isEmpty()) {
                entity.setSaleDetails(dto.getItems().stream()
                        .map(item -> {
                            SaleDetail detail = new SaleDetail();
                            detail.setItemId(item.getItemId());
                            detail.setQuantity(item.getQuantity());
                            detail.setSale(entity);
                            return detail;
                        })
                        .collect(Collectors.toList()));
            }
            
            // Save sale first to get saleId
            Sale savedEntity = saleRepository.save(entity);
            
            // Now create SaleTotal with the saleId
            SaleTotal saleTotal = new SaleTotal();
            saleTotal.setSaleId(savedEntity.getSaleId());
            saleTotal.setTotalAmount(dto.getTotalAmount() != null ? dto.getTotalAmount() : BigDecimal.ZERO);
            savedEntity.setSaleTotal(saleTotal);
            
            // Save again to persist SaleTotal
            Sale finalEntity = saleRepository.save(savedEntity);
            
            return saleMapper.toDTO(finalEntity);
        } catch (Exception e) {
            log.error("Error creating sale: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create sale: " + e.getMessage());
        }
    }

    @Override
    public SaleDTO update(Integer saleId, UpdateSaleRequestDTO dto) {
        Sale existingS = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        if(dto.getDiscountId()!=null && dto.getDiscountId() > 0){
            Customer existingC = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            existingS.setCustomer(existingC);
        }
        if(dto.getPaymentMethod() != null){
            existingS.setPaymentMethod(dto.getPaymentMethod());
        }
        if(dto.getDiscountId()!=null && dto.getDiscountId()>0) {
            if(!discountRepository.existsById(dto.getDiscountId())){
                throw new RuntimeException("Mã giảm giá '" + dto.getDiscountId() + "' không tồn tại");
            }
            existingS.setDiscountId(dto.getDiscountId());
        }
        if(dto.getPaymentMethod() != null){
            existingS.setPaymentMethod(dto.getPaymentMethod());
        }
        if(dto.getNote() != null){
            existingS.setNote(dto.getNote());
        }
        Sale savedEntity = saleRepository.save(existingS);
        return saleMapper.toDTO(savedEntity);
    }

    @Override
    public void delete(Integer saleId) {
        saleRepository.deleteById(saleId);
    }



    @Override
    public SaleDTO getSaleById(Integer saleId) {
        Sale s = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        return saleMapper.toDTO(s);
    }

    @Override
    public List<SaleDTO> searchSale(String sortBy, String sortOrder, Integer saleId, Integer customerId, String customerName) {
        List<Sale> sales = saleRepository.findAll();

        // Filter by saleId if provided
        if (saleId != null && saleId > 0) {
            sales = sales.stream()
                    .filter(sale -> sale.getSaleId().equals(saleId))
                    .collect(Collectors.toList());
        }

        // Filter by customerId if provided
        if (customerId != null && customerId > 0) {
            sales = sales.stream()
                    .filter(sale -> sale.getCustomer().getCustomerId().equals(customerId))
                    .collect(Collectors.toList());
        }

        // Filter by name if provided
        if (customerName != null && !customerName.trim().isEmpty()) {
            sales = sales.stream()
                    .filter(sale -> sale.getCustomer().getCustomerName().contains(customerName))
                    .collect(Collectors.toList());
        }

        // Sort by specified field and order
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            String order = (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) ? "desc" : "asc";

            switch (sortBy.toLowerCase()) {
                case "saleid":
                    sales = sales.stream()
                            .sorted((s1, s2) -> order.equals("desc") ?
                                    s2.getSaleId().compareTo(s1.getSaleId()) :
                                    s1.getSaleId().compareTo(s2.getSaleId()))
                            .collect(Collectors.toList());
                    break;
                case "date":
                    sales = sales.stream()
                            .sorted((s1, s2) -> order.equals("desc") ?
                                    s2.getSaleDate().compareTo(s1.getSaleDate()) :
                                    s1.getSaleDate().compareTo(s2.getSaleDate()))
                            .collect(Collectors.toList());
                    break;
                case "saletotal":
                    sales = sales.stream()
                            .sorted((s1, s2) -> order.equals("desc") ?
                                    s2.getSaleTotal().getTotalAmount().compareTo(s1.getSaleTotal().getTotalAmount()) :
                                    s1.getSaleTotal().getTotalAmount().compareTo(s2.getSaleTotal().getTotalAmount()))
                            .collect(Collectors.toList());
                    break;

                case "customername":
                    sales = sales.stream()
                            .sorted((s1, s2) -> order.equals("desc") ?
                                    s2.getCustomer().getCustomerName().compareTo(s1.getCustomer().getCustomerName()) :
                                    s1.getCustomer().getCustomerName().compareTo(s2.getCustomer().getCustomerName()))
                            .collect(Collectors.toList());
                    break;

                default:
                    // Default sort by saleId ascending
                    sales = sales.stream()
                            .sorted((s1, s2) -> s1.getSaleId().compareTo(s2.getSaleId()))
                            .collect(Collectors.toList());
                    break;
            }
        }

        return sales.stream()
                .map(saleMapper::toDTO)
                .collect(Collectors.toList());
    }

}
