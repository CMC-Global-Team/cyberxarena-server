package internetcafe_management.service.discount;

import internetcafe_management.dto.DiscountDTO;
import internetcafe_management.dto.UpdateDiscountRequestDTO;
import internetcafe_management.entity.Discount;
import java.util.List;
import java.util.Optional;

public interface DiscountService {
    Discount createDiscount(DiscountDTO discountDTO);
    Discount updateDiscount(Integer id, UpdateDiscountRequestDTO updateRequestDTO);
    List<Discount> getAllDiscounts();
    Optional<Discount> getDiscountById(Integer id);
}


