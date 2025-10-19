package internetcafe_management.controller;

import internetcafe_management.dto.ProductDTO;
import internetcafe_management.dto.UpdateProductRequestDTO;
import internetcafe_management.entity.Product;
import internetcafe_management.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
    
    private final ProductService productService;
    
    @PostMapping
    @Operation(summary = "Create new product", description = "Create a new product in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Product with same name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Product information", required = true)
            @Valid @RequestBody ProductDTO productDTO) {
        
        log.info("Received request to create product: {}", productDTO.getItemName());
        
        try {
            Product createdProduct = productService.createProduct(productDTO);
            log.info("Successfully created product with ID: {}", createdProduct.getItemId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
            
        } catch (RuntimeException e) {
            log.error("Error creating product: {}", e.getMessage());
            throw e;
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Retrieving all products");
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Integer id) {
        
        log.info("Retrieving product with ID: {}", id);
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping("/name/{itemName}")
    @Operation(summary = "Get product by name", description = "Retrieve a specific product by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> getProductByName(
            @Parameter(description = "Product name", required = true)
            @PathVariable String itemName) {
        
        log.info("Retrieving product with name: {}", itemName);
        Product product = productService.getProductByName(itemName);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping("/category/{itemCategory}")
    @Operation(summary = "Get products by category", description = "Retrieve products filtered by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "Product category", required = true)
            @PathVariable String itemCategory) {
        
        log.info("Retrieving products by category: {}", itemCategory);
        List<Product> products = productService.getProductsByCategory(itemCategory);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/supplier/{supplierName}")
    @Operation(summary = "Get products by supplier", description = "Retrieve products filtered by supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Product>> getProductsBySupplier(
            @Parameter(description = "Supplier name", required = true)
            @PathVariable String supplierName) {
        
        log.info("Retrieving products by supplier: {}", supplierName);
        List<Product> products = productService.getProductsBySupplier(supplierName);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Retrieve products with stock below threshold")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Product>> getLowStockProducts(
            @Parameter(description = "Stock threshold", required = true)
            @RequestParam(defaultValue = "10") Integer threshold) {
        
        log.info("Retrieving low stock products with threshold: {}", threshold);
        List<Product> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/price-range")
    @Operation(summary = "Get products by price range", description = "Retrieve products within specified price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid price range"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @Parameter(description = "Minimum price", required = true)
            @RequestParam java.math.BigDecimal minPrice,
            @Parameter(description = "Maximum price", required = true)
            @RequestParam java.math.BigDecimal maxPrice) {
        
        log.info("Retrieving products by price range: {} - {}", minPrice, maxPrice);
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update product completely", description = "Update all fields of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "Product with same name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Updated product information", required = true)
            @Valid @RequestBody UpdateProductRequestDTO updateRequest) {
        
        log.info("Received request to update product with ID: {}", id);
        
        try {
            Product updatedProduct = productService.updateProductWithRequest(id, updateRequest);
            log.info("Successfully updated product with ID: {}", updatedProduct.getItemId());
            
            return ResponseEntity.ok(updatedProduct);
            
        } catch (RuntimeException e) {
            log.error("Error updating product: {}", e.getMessage());
            throw e;
        }
    }
    
    @PatchMapping("/{id}")
    @Operation(summary = "Partially update product", description = "Update only specified fields of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "Product with same name already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> partialUpdateProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Partial product update information", required = true)
            @Valid @RequestBody UpdateProductRequestDTO updateRequest) {
        
        log.info("Received request to partially update product with ID: {}", id);
        
        try {
            Product updatedProduct = productService.partialUpdateProduct(id, updateRequest);
            log.info("Successfully partially updated product with ID: {}", updatedProduct.getItemId());
            
            return ResponseEntity.ok(updatedProduct);
            
        } catch (RuntimeException e) {
            log.error("Error partially updating product: {}", e.getMessage());
            throw e;
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Delete a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Integer id) {
        
        log.info("Received request to delete product with ID: {}", id);
        
        try {
            productService.deleteProduct(id);
            log.info("Successfully deleted product with ID: {}", id);
            
            return ResponseEntity.noContent().build();
            
        } catch (RuntimeException e) {
            log.error("Error deleting product: {}", e.getMessage());
            throw e;
        }
    }
    
    @PutMapping("/{id}/price")
    @Operation(summary = "Update product price", description = "Update only the price of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product price updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid price value"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> updateProductPrice(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Integer id,
            @Parameter(description = "New price", required = true)
            @RequestParam java.math.BigDecimal newPrice) {
        
        log.info("Received request to update price for product with ID: {} to {}", id, newPrice);
        
        try {
            Product updatedProduct = productService.updateProductPrice(id, newPrice);
            log.info("Successfully updated price for product with ID: {}", updatedProduct.getItemId());
            
            return ResponseEntity.ok(updatedProduct);
            
        } catch (RuntimeException e) {
            log.error("Error updating product price: {}", e.getMessage());
            throw e;
        }
    }
    
    @PutMapping("/{id}/stock")
    @Operation(summary = "Update product stock", description = "Update only the stock quantity of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product stock updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid stock value"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> updateProductStock(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Integer id,
            @Parameter(description = "New stock quantity", required = true)
            @RequestParam Integer newStock) {
        
        log.info("Received request to update stock for product with ID: {} to {}", id, newStock);
        
        try {
            Product updatedProduct = productService.updateProductStock(id, newStock);
            log.info("Successfully updated stock for product with ID: {}", updatedProduct.getItemId());
            
            return ResponseEntity.ok(updatedProduct);
            
        } catch (RuntimeException e) {
            log.error("Error updating product stock: {}", e.getMessage());
            throw e;
        }
    }
    
    @PutMapping("/{id}/supplier")
    @Operation(summary = "Update product supplier", description = "Update only the supplier of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product supplier updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> updateProductSupplier(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Integer id,
            @Parameter(description = "New supplier name", required = true)
            @RequestParam String supplierName) {
        
        log.info("Received request to update supplier for product with ID: {} to {}", id, supplierName);
        
        try {
            Product updatedProduct = productService.updateProductSupplier(id, supplierName);
            log.info("Successfully updated supplier for product with ID: {}", updatedProduct.getItemId());
            
            return ResponseEntity.ok(updatedProduct);
            
        } catch (RuntimeException e) {
            log.error("Error updating product supplier: {}", e.getMessage());
            throw e;
        }
    }
    
    @PutMapping("/{id}/category")
    @Operation(summary = "Update product category", description = "Update only the category of a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Product> updateProductCategory(
            @Parameter(description = "Product ID", required = true)
            @PathVariable Integer id,
            @Parameter(description = "New category", required = true)
            @RequestParam String category) {
        
        log.info("Received request to update category for product with ID: {} to {}", id, category);
        
        try {
            Product updatedProduct = productService.updateProductCategory(id, category);
            log.info("Successfully updated category for product with ID: {}", updatedProduct.getItemId());
            
            return ResponseEntity.ok(updatedProduct);
            
        } catch (RuntimeException e) {
            log.error("Error updating product category: {}", e.getMessage());
            throw e;
        }
    }
}
