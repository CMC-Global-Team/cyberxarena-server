package internetcafe_management.service;

import internetcafe_management.dto.CreateMembershipCardRequestDTO;
import internetcafe_management.dto.MembershipCardDTO;
import internetcafe_management.dto.UpdateMembershipCardRequestDTO;
import internetcafe_management.entity.MembershipCard;
import internetcafe_management.repository.MembershipCardRepository;
import internetcafe_management.repository.discount.DiscountRepository;
import internetcafe_management.exception.ResourceNotFoundException;
import internetcafe_management.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MembershipCardService {
    
    @Autowired
    private MembershipCardRepository membershipCardRepository;
    
    @Autowired
    private DiscountRepository discountRepository;
    
    public MembershipCardDTO createMembershipCard(CreateMembershipCardRequestDTO request) {
        // Check if membership card name already exists
        if (membershipCardRepository.existsByMembershipCardName(request.getMembershipCardName())) {
            throw new DuplicateResourceException("Membership card with name '" + request.getMembershipCardName() + "' already exists");
        }
        
        // Validate discount if provided
        if (request.getDiscountId() != null) {
            if (!discountRepository.existsById(request.getDiscountId())) {
                throw new ResourceNotFoundException("Discount with ID " + request.getDiscountId() + " not found");
            }
        }
        
        MembershipCard membershipCard = new MembershipCard();
        membershipCard.setMembershipCardName(request.getMembershipCardName());
        membershipCard.setDiscountId(request.getDiscountId());
        
        MembershipCard savedMembershipCard = membershipCardRepository.save(membershipCard);
        
        // Force flush to ensure the data is persisted before fetching
        membershipCardRepository.flush();
        
        // Clear the persistence context to force fresh data fetch
        // This ensures we get the latest data from database
        if (request.getDiscountId() != null) {
            // Manually load the discount and set it
            var discount = discountRepository.findById(request.getDiscountId()).orElse(null);
            savedMembershipCard.setDiscount(discount);
        }
        
        return convertToDTO(savedMembershipCard);
    }
    
    public MembershipCardDTO getMembershipCardById(Integer id) {
        MembershipCard membershipCard = membershipCardRepository.findByIdWithDiscount(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership card with ID " + id + " not found"));
        return convertToDTO(membershipCard);
    }
    
    public List<MembershipCardDTO> getAllMembershipCards() {
        List<MembershipCard> membershipCards = membershipCardRepository.findAllWithDiscount();
        return membershipCards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public MembershipCardDTO updateMembershipCard(Integer id, UpdateMembershipCardRequestDTO request) {
        MembershipCard membershipCard = membershipCardRepository.findByIdWithDiscount(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership card with ID " + id + " not found"));
        
        // Check if new name already exists (excluding current record)
        if (!membershipCard.getMembershipCardName().equals(request.getMembershipCardName()) &&
            membershipCardRepository.existsByMembershipCardName(request.getMembershipCardName())) {
            throw new DuplicateResourceException("Membership card with name '" + request.getMembershipCardName() + "' already exists");
        }
        
        // Validate discount if provided
        if (request.getDiscountId() != null) {
            if (!discountRepository.existsById(request.getDiscountId())) {
                throw new ResourceNotFoundException("Discount with ID " + request.getDiscountId() + " not found");
            }
        }
        
        membershipCard.setMembershipCardName(request.getMembershipCardName());
        membershipCard.setDiscountId(request.getDiscountId());
        
        MembershipCard savedMembershipCard = membershipCardRepository.save(membershipCard);
        
        // Force flush to ensure the data is persisted before fetching
        membershipCardRepository.flush();
        
        // Manually load the discount and set it
        if (request.getDiscountId() != null) {
            var discount = discountRepository.findById(request.getDiscountId()).orElse(null);
            savedMembershipCard.setDiscount(discount);
        }
        
        return convertToDTO(savedMembershipCard);
    }
    
    public void deleteMembershipCard(Integer id) {
        if (!membershipCardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Membership card with ID " + id + " not found");
        }
        membershipCardRepository.deleteById(id);
    }
    
    private MembershipCardDTO convertToDTO(MembershipCard membershipCard) {
        MembershipCardDTO dto = new MembershipCardDTO();
        dto.setMembershipCardId(membershipCard.getMembershipCardId());
        dto.setMembershipCardName(membershipCard.getMembershipCardName());
        dto.setDiscountId(membershipCard.getDiscountId());
        
        if (membershipCard.getDiscount() != null) {
            dto.setDiscountName(membershipCard.getDiscount().getDiscountName());
            dto.setDiscountType(membershipCard.getDiscount().getDiscountType().toString());
            dto.setDiscountValue(membershipCard.getDiscount().getDiscountValue().doubleValue());
        }
        
        return dto;
    }
}
