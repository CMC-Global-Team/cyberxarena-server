package internetcafe_management.service.impl.discount;

import internetcafe_management.dto.DiscountDTO;
import internetcafe_management.entity.Discount;
import internetcafe_management.repository.discount.DiscountRepository;
import internetcafe_management.service.discount.DiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Override
    public Discount createDiscount(DiscountDTO discountDTO) {
        log.info("Creating new discount: type={}, value={}", discountDTO.getDiscount_type(), discountDTO.getDiscount_value());

        Discount discount = new Discount();
        discount.setDiscountType(discountDTO.getDiscount_type());
        discount.setDiscountValue(discountDTO.getDiscount_value());

        Discount saved = discountRepository.save(discount);
        log.info("Successfully created discount with ID: {}", saved.getDiscountId());
        return saved;
    }
}


