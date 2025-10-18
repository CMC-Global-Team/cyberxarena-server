package internetcafe_management.specification;

import internetcafe_management.entity.Computer;
import internetcafe_management.entity.Computer.ComputerStatus;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ComputerSpecification {

    public static Specification<Computer> search(String name, String ip, String status) {
        return (root, query, criteriaBuilder) -> {
            // Tạo một danh sách để chứa các điều kiện
            List<Predicate> predicates = new ArrayList<>();

            //Thêm điều kiện tìm kiếm theo Tên máy
            if (name != null && !name.trim().isEmpty()) {
                // Tương đương với: WHERE LOWER(computer_name) LIKE '%name%'
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("computerName")), "%" + name.toLowerCase() + "%"));
            }

            //Thêm điều kiện tìm kiếm theo IP
            if (ip != null && !ip.trim().isEmpty()) {
                // Tương đương với: WHERE ip_address LIKE '%ip%'
                predicates.add(criteriaBuilder.like(root.get("ipAddress"), "%" + ip + "%"));
            }

            //Thêm điều kiện tìm kiếm theo Trạng thái
            if (status != null && !status.trim().isEmpty()) {
                try {
                    // Chuyển chuỗi trạng thái thành Enum
                    ComputerStatus computerStatus = ComputerStatus.valueOf(status);
                    // Tương đương với: WHERE status = 'status'
                    predicates.add(criteriaBuilder.equal(root.get("status"), computerStatus));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Giá trị trạng thái không hợp lệ: " + status);
                }
            }

            // Kết hợp tất cả các điều kiện lại bằng mệnh đề AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}