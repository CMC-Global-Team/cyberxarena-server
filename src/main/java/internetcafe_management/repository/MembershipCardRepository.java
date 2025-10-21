package internetcafe_management.repository;

import internetcafe_management.entity.MembershipCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipCardRepository extends JpaRepository<MembershipCard, Integer> {
    
    Optional<MembershipCard> findByMembershipCardName(String membershipCardName);
    
    boolean existsByMembershipCardName(String membershipCardName);
    
    @Query("SELECT mc FROM MembershipCard mc LEFT JOIN FETCH mc.discount WHERE mc.membershipCardId = :id")
    Optional<MembershipCard> findByIdWithDiscount(@Param("id") Integer id);
    
    @Query("SELECT mc FROM MembershipCard mc LEFT JOIN FETCH mc.discount")
    List<MembershipCard> findAllWithDiscount();
    
    Optional<MembershipCard> findByIsDefaultTrue();
    
    @Modifying
    @Transactional
    @Query("UPDATE MembershipCard mc SET mc.isDefault = false WHERE mc.isDefault = true")
    void unsetAllDefaultCards();
}
