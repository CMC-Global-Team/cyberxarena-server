package internetcafe_management.service;

import internetcafe_management.entity.Discount;
import internetcafe_management.entity.MembershipCard;
import internetcafe_management.repository.MembershipCardRepository;
import internetcafe_management.repository.discount.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service để khởi tạo dữ liệu membership card mặc định
 */
@Service
public class MembershipInitializationService implements CommandLineRunner {
    
    @Autowired
    private MembershipCardRepository membershipCardRepository;
    
    @Autowired
    private DiscountRepository discountRepository;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeDefaultMembershipCards();
    }
    
    /**
     * Khởi tạo các membership card mặc định
     */
    private void initializeDefaultMembershipCards() {
        // Kiểm tra xem đã có membership card nào chưa
        List<MembershipCard> existingCards = membershipCardRepository.findAll();
        if (!existingCards.isEmpty()) {
            System.out.println("Membership cards already exist, skipping initialization");
            return;
        }
        
        System.out.println("Initializing default membership cards...");
        
        // Tạo discount mặc định
        Discount noDiscount = createDiscountIfNotExists("No Discount", Discount.DiscountType.Flat, BigDecimal.ZERO);
        Discount bronzeDiscount = createDiscountIfNotExists("Bronze 5%", Discount.DiscountType.Percentage, BigDecimal.valueOf(5));
        Discount silverDiscount = createDiscountIfNotExists("Silver 10%", Discount.DiscountType.Percentage, BigDecimal.valueOf(10));
        Discount goldDiscount = createDiscountIfNotExists("Gold 15%", Discount.DiscountType.Percentage, BigDecimal.valueOf(15));
        Discount platinumDiscount = createDiscountIfNotExists("Platinum 20%", Discount.DiscountType.Percentage, BigDecimal.valueOf(20));
        
        // Tạo membership cards
        createMembershipCardIfNotExists("Basic", noDiscount.getDiscountId(), BigDecimal.ZERO, true);
        createMembershipCardIfNotExists("Bronze", bronzeDiscount.getDiscountId(), BigDecimal.valueOf(100000));
        createMembershipCardIfNotExists("Silver", silverDiscount.getDiscountId(), BigDecimal.valueOf(500000));
        createMembershipCardIfNotExists("Gold", goldDiscount.getDiscountId(), BigDecimal.valueOf(1000000));
        createMembershipCardIfNotExists("Platinum", platinumDiscount.getDiscountId(), BigDecimal.valueOf(2000000));
        
        System.out.println("Default membership cards initialized successfully");
    }
    
    /**
     * Tạo discount nếu chưa tồn tại
     */
    private Discount createDiscountIfNotExists(String discountName, Discount.DiscountType discountType, BigDecimal discountValue) {
        return discountRepository.findByDiscountName(discountName)
                .orElseGet(() -> {
                    Discount discount = new Discount(discountName, discountType, discountValue);
                    return discountRepository.save(discount);
                });
    }
    
    /**
     * Tạo membership card nếu chưa tồn tại
     */
    private MembershipCard createMembershipCardIfNotExists(String cardName, Integer discountId, BigDecimal rechargeThreshold) {
        return createMembershipCardIfNotExists(cardName, discountId, rechargeThreshold, false);
    }
    
    /**
     * Tạo membership card nếu chưa tồn tại
     */
    private MembershipCard createMembershipCardIfNotExists(String cardName, Integer discountId, BigDecimal rechargeThreshold, boolean isDefault) {
        return membershipCardRepository.findByMembershipCardName(cardName)
                .orElseGet(() -> {
                    MembershipCard card = new MembershipCard(cardName, discountId, rechargeThreshold, isDefault);
                    return membershipCardRepository.save(card);
                });
    }
}
