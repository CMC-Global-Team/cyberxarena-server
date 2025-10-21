package internetcafe_management.service.impl.sale;

import internetcafe_management.dto.SaleDTO;
import internetcafe_management.entity.Customer;
import internetcafe_management.entity.Sale;
import internetcafe_management.mapper.Customer.CustomerMapper;
import internetcafe_management.mapper.sale.SaleMapper;
import internetcafe_management.repository.Customer.CustomerRepository;
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
    @Override
    public Sale create(SaleDTO dto, Integer customer) {

        Sale entity = saleMapper.toEntity(dto,customerMapper.toEntity(customerService.getCustomerById(customer)));
        return saleRepository.save(entity);
    }
}
