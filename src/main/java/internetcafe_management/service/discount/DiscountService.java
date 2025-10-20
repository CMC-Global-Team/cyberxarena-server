package internetcafe_management.service.discount;

import internetcafe_management.dto.DiscountDTO;
import internetcafe_management.dto.UpdateDiscountRequestDTO;
import internetcafe_management.entity.Discount;

public interface DiscountService {
    Discount createDiscount(DiscountDTO discountDTO);
    Discount updateDiscount(Integer id, UpdateDiscountRequestDTO updateRequestDTO);
}


