package internetcafe_management.specification;

import internetcafe_management.entity.Account;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class AccountSpecification {

    public static Specification<Account> search(String username, String customerName, String phoneNumber, String membershipCard) {
        return (root, query, criteriaBuilder) -> {
            // Tạo một danh sách để chứa các điều kiện
            List<Predicate> predicates = new ArrayList<>();

            // Thêm điều kiện tìm kiếm theo Username
            if (username != null && !username.trim().isEmpty()) {
                // Tương đương với: WHERE LOWER(username) LIKE '%username%'
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }

            // Thêm điều kiện tìm kiếm theo Customer Name
            if (customerName != null && !customerName.trim().isEmpty()) {
                // Join với bảng Customer và tìm kiếm theo customer_name
                // Tương đương với: WHERE LOWER(customer.customer_name) LIKE '%customerName%'
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("customer").get("customerName")), 
                    "%" + customerName.toLowerCase() + "%"
                ));
            }

            // Thêm điều kiện tìm kiếm theo Phone Number
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                // Join với bảng Customer và tìm kiếm theo phone_number
                // Tương đương với: WHERE customer.phone_number LIKE '%phoneNumber%'
                predicates.add(criteriaBuilder.like(
                    root.get("customer").get("phoneNumber"), 
                    "%" + phoneNumber + "%"
                ));
            }

            // Thêm điều kiện tìm kiếm theo Membership Card
            if (membershipCard != null && !membershipCard.trim().isEmpty()) {
                // Join với bảng Customer và tìm kiếm theo membership_card
                // Tương đương với: WHERE customer.membership_card LIKE '%membershipCard%'
                predicates.add(criteriaBuilder.like(
                    root.get("customer").get("membershipCard"), 
                    "%" + membershipCard + "%"
                ));
            }

            // Kết hợp tất cả các điều kiện lại bằng mệnh đề AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
