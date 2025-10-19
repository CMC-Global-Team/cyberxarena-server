package internetcafe_management.repository.product;

import internetcafe_management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    /**
     * Tìm sản phẩm theo tên
     */
    Optional<Product> findByItemName(String itemName);
    
    /**
     * Kiểm tra sản phẩm có tồn tại theo tên không
     */
    boolean existsByItemName(String itemName);
    
    /**
     * Tìm sản phẩm theo danh mục
     */
    List<Product> findByItemCategory(String itemCategory);
    
    /**
     * Tìm sản phẩm theo nhà cung cấp
     */
    List<Product> findBySupplierName(String supplierName);
    
    /**
     * Tìm sản phẩm có số lượng tồn kho thấp
     */
    @Query("SELECT p FROM Product p WHERE p.stock <= :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
    
    /**
     * Tìm sản phẩm theo khoảng giá
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, 
                                   @Param("maxPrice") java.math.BigDecimal maxPrice);
    
    /**
     * Tìm tất cả sản phẩm nổi bật
     */
    List<Product> findByIsFeaturedTrue();
    
    /**
     * Tìm sản phẩm nổi bật theo danh mục
     */
    List<Product> findByIsFeaturedTrueAndItemCategory(String itemCategory);
    
    /**
     * Đếm số lượng sản phẩm nổi bật
     */
    long countByIsFeaturedTrue();
}
