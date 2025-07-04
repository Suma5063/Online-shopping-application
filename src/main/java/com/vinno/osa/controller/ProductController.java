package com.vinno.osa.controller;
import com.vinno.osa.entity.Product;
import com.vinno.osa.exception.ProductNotFoundException;
import com.vinno.osa.repository.ProductRepository;
import com.vinno.osa.service.ProductService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);  // Initialize the logger

    // Add a new product
    @PreAuthorize("hasAnyRole('ROLE_SELLER','ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_BUYER')")
    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable String productId) {
        return productService.getProductById(productId); // This will trigger the exception if not found
     }


    @GetMapping("/searchByName")  // Search products by name
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(List.of());         // Return empty list with a 400 status if the search name is invalid
        }
        try {
            List<Product> products = productService.searchProductsByName(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(404).body(List.of());      // Return empty list with a 404 status if no products are found
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();                                     // Log the actual exception details
            return ResponseEntity.status(500).body(List.of());      // Return empty list with a 500 status and error message
        }
    }

    // Basic Search with a query
    @GetMapping("/search")
    public List<Product> search(@RequestParam String query) {
       return productService.search(query);
    }

    // Filter products by price range
    @GetMapping("/filterByPrice")
    public ResponseEntity<List<Product>> filterProductsByPrice(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(productService.filterProductsByPrice(minPrice, maxPrice));
    }

    // Get products by brand
    @GetMapping("/brand")
    public ResponseEntity<List<Product>> getProductsByBrand(@RequestParam String brand) {
        return ResponseEntity.ok(productService.getProductsByBrand(brand));
    }

  @GetMapping("/categories")
  public ResponseEntity<List<Product>> getProductsByCategories(
          @RequestParam List<String> categories,
          @RequestParam(required = false) String minPrice,
          @RequestParam(required = false) String maxPrice) {
      try {
          BigDecimal minPriceDecimal = (minPrice != null && isValidDecimal(minPrice)) ? new BigDecimal(minPrice) : null;
          BigDecimal maxPriceDecimal = (maxPrice != null && isValidDecimal(maxPrice)) ? new BigDecimal(maxPrice) : null;

          List<Product> products = productService.getProductsByCategoriesAndPrice(categories, minPriceDecimal, maxPriceDecimal);
          return ResponseEntity.ok(products);
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
      }
  }

    private boolean isValidDecimal(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Get all products with pagination
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Product> products = productService.getAllProducts(page, size);
        return ResponseEntity.ok(products);
    }

    // Get products by category
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategoriesContaining(category);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable String productId) {
        try {
            Product deletedProduct = productService.deleteProduct(productId);
            return ResponseEntity.ok(deletedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
