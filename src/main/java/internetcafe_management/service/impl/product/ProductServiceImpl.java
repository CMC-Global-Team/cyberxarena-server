package internetcafe_management.service.impl.product;

import internetcafe_management.dto.ProductDTO;
import internetcafe_management.dto.UpdateProductRequestDTO;
import internetcafe_management.entity.Product;
import internetcafe_management.repository.product.ProductRepository;
import internetcafe_management.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    public Product createProduct(ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO.getItemName());
        
        // Kiểm tra sản phẩm đã tồn tại chưa
        if (productRepository.existsByItemName(productDTO.getItemName())) {
            throw new RuntimeException("Sản phẩm với tên '" + productDTO.getItemName() + "' đã tồn tại");
        }
        
        // Tạo entity từ DTO
        Product product = new Product();
        product.setItemName(productDTO.getItemName());
        product.setItemCategory(productDTO.getItemCategory());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock() != null ? productDTO.getStock() : 0);
        product.setSupplierName(productDTO.getSupplierName());
        
        Product savedProduct = productRepository.save(product);
        log.info("Successfully created product with ID: {}", savedProduct.getItemId());
        
        return savedProduct;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        log.info("Retrieving all products");
        return productRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Integer id) {
        log.info("Retrieving product with ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
    }
    
    @Override
    public Product updateProduct(Integer id, ProductDTO productDTO) {
        log.info("Updating product with ID: {}", id);
        
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
        
        // Kiểm tra tên sản phẩm có bị trùng không (nếu thay đổi tên)
        if (!existingProduct.getItemName().equals(productDTO.getItemName()) 
            && productRepository.existsByItemName(productDTO.getItemName())) {
            throw new RuntimeException("Sản phẩm với tên '" + productDTO.getItemName() + "' đã tồn tại");
        }
        
        existingProduct.setItemName(productDTO.getItemName());
        existingProduct.setItemCategory(productDTO.getItemCategory());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setStock(productDTO.getStock());
        existingProduct.setSupplierName(productDTO.getSupplierName());
        
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Successfully updated product with ID: {}", updatedProduct.getItemId());
        
        return updatedProduct;
    }
    
    @Override
    public void deleteProduct(Integer id) {
        log.info("Deleting product with ID: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sản phẩm với ID: " + id);
        }
        
        productRepository.deleteById(id);
        log.info("Successfully deleted product with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Product getProductByName(String itemName) {
        log.info("Retrieving product by name: {}", itemName);
        return productRepository.findByItemName(itemName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với tên: " + itemName));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String itemCategory) {
        log.info("Retrieving products by category: {}", itemCategory);
        return productRepository.findByItemCategory(itemCategory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsBySupplier(String supplierName) {
        log.info("Retrieving products by supplier: {}", supplierName);
        return productRepository.findBySupplierName(supplierName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts(Integer threshold) {
        log.info("Retrieving low stock products with threshold: {}", threshold);
        return productRepository.findLowStockProducts(threshold);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Retrieving products by price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    @Override
    public Product updateProductWithRequest(Integer id, UpdateProductRequestDTO updateRequest) {
        log.info("Updating product with ID: {} using UpdateProductRequestDTO", id);
        
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
        
        // Kiểm tra tên sản phẩm có bị trùng không (nếu thay đổi tên)
        if (!existingProduct.getItemName().equals(updateRequest.getItemName()) 
            && productRepository.existsByItemName(updateRequest.getItemName())) {
            throw new RuntimeException("Sản phẩm với tên '" + updateRequest.getItemName() + "' đã tồn tại");
        }
        
        // Cập nhật tất cả các trường
        existingProduct.setItemName(updateRequest.getItemName());
        existingProduct.setItemCategory(updateRequest.getItemCategory());
        existingProduct.setPrice(updateRequest.getPrice());
        existingProduct.setStock(updateRequest.getStock());
        existingProduct.setSupplierName(updateRequest.getSupplierName());
        
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Successfully updated product with ID: {} using UpdateProductRequestDTO", updatedProduct.getItemId());
        
        return updatedProduct;
    }
    
    @Override
    public Product partialUpdateProduct(Integer id, UpdateProductRequestDTO updateRequest) {
        log.info("Partially updating product with ID: {}", id);
        
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
        
        // Chỉ cập nhật các trường không null
        if (updateRequest.getItemName() != null && !updateRequest.getItemName().trim().isEmpty()) {
            // Kiểm tra tên sản phẩm có bị trùng không (nếu thay đổi tên)
            if (!existingProduct.getItemName().equals(updateRequest.getItemName()) 
                && productRepository.existsByItemName(updateRequest.getItemName())) {
                throw new RuntimeException("Sản phẩm với tên '" + updateRequest.getItemName() + "' đã tồn tại");
            }
            existingProduct.setItemName(updateRequest.getItemName());
        }
        
        if (updateRequest.getItemCategory() != null) {
            existingProduct.setItemCategory(updateRequest.getItemCategory());
        }
        
        if (updateRequest.getPrice() != null) {
            existingProduct.setPrice(updateRequest.getPrice());
        }
        
        if (updateRequest.getStock() != null) {
            existingProduct.setStock(updateRequest.getStock());
        }
        
        if (updateRequest.getSupplierName() != null) {
            existingProduct.setSupplierName(updateRequest.getSupplierName());
        }
        
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Successfully partially updated product with ID: {}", updatedProduct.getItemId());
        
        return updatedProduct;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByItemName(String itemName) {
        return productRepository.existsByItemName(itemName);
    }
}
