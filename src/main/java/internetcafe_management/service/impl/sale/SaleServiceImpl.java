package internetcafe_management.service.impl.sale;

import internetcafe_management.dto.SaleDTO;
import internetcafe_management.dto.UpdateSaleRequestDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.Sale;
import internetcafe_management.mapper.Customer.CustomerMapper;
import internetcafe_management.mapper.sale.SaleMapper;
import internetcafe_management.repository.Customer.CustomerRepository;
import internetcafe_management.repository.discount.DiscountRepository;
import internetcafe_management.repository.sale.SaleRepository;
import internetcafe_management.service.Customer.CustomerService;
import internetcafe_management.service.sale.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Sale create(SaleDTO dto, Integer customer) {
        Sale entity = saleMapper.toEntity(dto,customerMapper.toEntity(customerService.getCustomerById(customer)));
        return saleRepository.save(entity);
    }

    @Override
    public Sale update(Integer saleId, UpdateSaleRequestDTO dto) {
        Sale existingS = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        if(dto.getDiscountId()!=null){
            Customer existingC = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            existingS.setCustomer(existingC);
        }
        if(dto.getPaymentMethod() != null){
            existingS.setPaymentMethod(dto.getPaymentMethod());
        }
        if(dto.getDiscountId()!=null) {
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
        return null;
    }
}
