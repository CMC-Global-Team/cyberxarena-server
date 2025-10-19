package internetcafe_management.service.product;

import internetcafe_management.dto.ProductDTO;
import internetcafe_management.dto.UpdateProductRequestDTO;
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
     * Cập nhật sản phẩm với UpdateProductRequestDTO
     */
    Product updateProductWithRequest(Integer id, UpdateProductRequestDTO updateRequest);
    
    /**
     * Cập nhật một phần thông tin sản phẩm (partial update)
     */
    Product partialUpdateProduct(Integer id, UpdateProductRequestDTO updateRequest);
    
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
    
    /**
     * Cập nhật giá sản phẩm
     */
    Product updateProductPrice(Integer id, java.math.BigDecimal newPrice);
    
    /**
     * Cập nhật số lượng tồn kho sản phẩm
     */
    Product updateProductStock(Integer id, Integer newStock);
    
    /**
     * Cập nhật thông tin nhà cung cấp
     */
    Product updateProductSupplier(Integer id, String supplierName);
    
    /**
     * Cập nhật danh mục sản phẩm
     */
    Product updateProductCategory(Integer id, String category);
    
    /**
     * Kiểm tra sản phẩm có tồn tại theo ID không
     */
    boolean existsById(Integer id);
}
