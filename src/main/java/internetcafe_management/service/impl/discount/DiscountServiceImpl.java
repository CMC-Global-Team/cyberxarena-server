package internetcafe_management.service.impl.discount;

import internetcafe_management.dto.DiscountDTO;
import internetcafe_management.dto.UpdateDiscountRequestDTO;
import internetcafe_management.entity.Discount;
import internetcafe_management.repository.discount.DiscountRepository;
import internetcafe_management.service.discount.DiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Override
    public Discount createDiscount(DiscountDTO discountDTO) {
        log.info("Creating new discount: name={}, type={}, value={}", discountDTO.getDiscount_name(), discountDTO.getDiscount_type(), discountDTO.getDiscount_value());

        Discount discount = new Discount();
        discount.setDiscountName(discountDTO.getDiscount_name());
        discount.setDiscountType(discountDTO.getDiscount_type());
        discount.setDiscountValue(discountDTO.getDiscount_value());

        Discount saved = discountRepository.save(discount);
        log.info("Successfully created discount with ID: {}", saved.getDiscountId());
        return saved;
    }

    @Override
    public Discount updateDiscount(Integer id, UpdateDiscountRequestDTO updateRequestDTO) {
        log.info("Updating discount id={}", id);

        Optional<Discount> existingOpt = discountRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Discount not found with id=" + id);
        }

        Discount existing = existingOpt.get();

        if (updateRequestDTO.getDiscount_name() != null) {
            existing.setDiscountName(updateRequestDTO.getDiscount_name());
        }
        if (updateRequestDTO.getDiscount_type() != null) {
            existing.setDiscountType(updateRequestDTO.getDiscount_type());
        }
        if (updateRequestDTO.getDiscount_value() != null) {
            existing.setDiscountValue(updateRequestDTO.getDiscount_value());
        }

        Discount saved = discountRepository.save(existing);
        log.info("Updated discount id={} successfully", id);
        return saved;
    }

    @Override
    public List<Discount> getAllDiscounts() {
        log.info("Fetching all discounts");
        return discountRepository.findAll();
    }

    @Override
    public Optional<Discount> getDiscountById(Integer id) {
        log.info("Fetching discount by id={}", id);
        return discountRepository.findById(id);
    }

    @Override
    public List<Discount> getDiscountsByType(Discount.DiscountType type) {
        log.info("Fetching discounts by type={}", type);
        return discountRepository.findByDiscountType(type);
    }

    @Override
    public Map<String, Object> checkDiscountUsage(Integer id) {
        log.info("Checking discount usage for id={}", id);
        
        if (!discountRepository.existsById(id)) {
            throw new IllegalArgumentException("Discount not found with id=" + id);
        }
        
        // Check if discount is used by any membership cards
        List<Object[]> usageResults = discountRepository.findMembershipCardsUsingDiscount(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("isUsed", !usageResults.isEmpty());
        result.put("usageCount", usageResults.size());
        result.put("membershipCards", usageResults.stream()
            .map(row -> {
                Map<String, Object> card = new HashMap<>();
                card.put("membershipCardId", row[0]);
                card.put("membershipCardName", row[1]);
                card.put("rechargeThreshold", row[2]);
                return card;
            })
            .toList());
        
        log.info("Discount usage check completed for id={}, isUsed={}, count={}", 
                id, result.get("isUsed"), result.get("usageCount"));
        
        return result;
    }

    @Override
    public void deleteDiscount(Integer id) {
        log.info("Deleting discount with id={}", id);
        
        if (!discountRepository.existsById(id)) {
            throw new IllegalArgumentException("Discount not found with id=" + id);
        }
        
        discountRepository.deleteById(id);
        log.info("Successfully deleted discount with id={}", id);
    }
}


