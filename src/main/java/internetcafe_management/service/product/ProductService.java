package internetcafe_management.service.product;

import internetcafe_management.dto.ProductDTO;
import internetcafe_management.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    
    /**
     * Tạo sản phẩm mới
     */
    Product createProduct(ProductDTO productDTO);
    
    /**
     * Lấy tất cả sản phẩm
     */
    List<Product> getAllProducts();
    
    /**
     * Lấy sản phẩm theo ID
     */
    Product getProductById(Integer id);
    
    /**
     * Cập nhật sản phẩm
     */
    Product updateProduct(Integer id, ProductDTO productDTO);
    
    /**
     * Xóa sản phẩm
     */
    void deleteProduct(Integer id);
    
    /**
     * Tìm sản phẩm theo tên
     */
    Product getProductByName(String itemName);
    
    /**
     * Tìm sản phẩm theo danh mục
     */
    List<Product> getProductsByCategory(String itemCategory);
    
    /**
     * Tìm sản phẩm theo nhà cung cấp
     */
    List<Product> getProductsBySupplier(String supplierName);
    
    /**
     * Tìm sản phẩm có số lượng tồn kho thấp
     */
    List<Product> getLowStockProducts(Integer threshold);
    
    /**
     * Tìm sản phẩm theo khoảng giá
     */
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Kiểm tra sản phẩm có tồn tại không
     */
    boolean existsByItemName(String itemName);
}
