package com.vinno.osa.service;
import com.vinno.osa.entity.Product;
import com.vinno.osa.exception.ProductNotFoundException;
import com.vinno.osa.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);  // Initialize the logger

     // Search products by name
    public List<Product> searchProductsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty.");
        }
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // Filter products by price range
    public List<Product> filterProductsByPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // Get products by brand
    public List<Product> getProductsByBrand(String brand) {

        return productRepository.findByBrand(brand);
    }

    public List<Product> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public Product createProduct(Product product) {
        if (product.getStockDetails().getAvailableStock() == 0) {
            throw new RuntimeException("Stock cannot be zero when adding a product");
        }
        return productRepository.save(product);
    }

    // Method to get related products by category
    public List<Product> getRelatedProducts(String category) {
        return productRepository.findByCategory(category);
    }


    public List<Product> search(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    public List<Product> getTrendingProducts() {
        return productRepository.findByTrendingTrue();
    }

    public List<Product> getFilteredProducts(String category, Double minPrice, Double maxPrice, String brand, Double rating, String color, String size) {
        return productRepository.findAll().stream()
                .filter(p -> (category == null || (Objects.nonNull(p.getCategory()) && p.getCategory().equalsIgnoreCase(category))))
                .filter(p -> (brand == null || (Objects.nonNull(p.getBrand()) && p.getBrand().equalsIgnoreCase(brand))))
                //.filter(p -> (color == null || (Objects.nonNull(p.getColor()) && p.getColor().equalsIgnoreCase(color))))
                .filter(p -> (rating == null || p.getRating() >= rating))
                .filter(p -> (minPrice == null || (p.getPrice().compareTo(BigDecimal.valueOf(minPrice)) >= 0)))
                .filter(p -> (maxPrice == null || (p.getPrice().compareTo(BigDecimal.valueOf(maxPrice)) <= 0)))
                .collect(Collectors.toList());
    }

//   public Page<Product> getFilteredProducts(String category, Double minPrice, Double maxPrice, String brand, Double rating, String color, String size, int page, int sizePerPage, String sortBy, String sortDirection) {
//        Pageable pageable = PageRequest.of(page, sizePerPage, Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
//        return productRepository.findByCategoryIgnoreCaseContainingAndBrandIgnoreCaseContainingAndColorIgnoreCaseContainingAndSizeContainingAndPriceBetweenAndRatingGreaterThanEqual(
//                category != null ? category : "",
//                brand != null ? brand : "",
//                color != null ? color : "",
//                size != null ? size : "",
//                minPrice != null ? BigDecimal.valueOf(minPrice) : BigDecimal.ZERO,
//                maxPrice != null ? BigDecimal.valueOf(maxPrice) : BigDecimal.valueOf(Double.MAX_VALUE),
//                rating != null ? rating : 0.0, pageable);
//    }

    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int page, int sizePerPage) {
        Pageable pageable = PageRequest.of(page, sizePerPage);
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product deleteProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.deleteById(productId);
        return product;
    }

    public List<Product> getProductsByCategoriesAndPrice(List<String> categories, BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null) {
            minPrice = BigDecimal.ZERO;  // If not provided, start from 0
        }
        if (maxPrice == null) {
            maxPrice = BigDecimal.valueOf(Double.MAX_VALUE); // Default to a large number if not provided
        }
        logger.info("Fetching products with categories: {}, minPrice: {}, maxPrice: {}", categories, minPrice, maxPrice);
        List<Product> products = productRepository.findByCategoriesAndPriceRange(categories, minPrice, maxPrice);
        //logger.info("Products returned: {}", products.size());

        return products;
    }


    public Product getProductById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));
    }
}
