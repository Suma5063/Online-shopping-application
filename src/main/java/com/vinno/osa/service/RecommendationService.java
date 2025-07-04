package com.vinno.osa.service;
import com.vinno.osa.entity.Product;
import com.vinno.osa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecommendationService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getRecommendedProducts(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Example logic: Recommend products in the same category or purchased together
        List<String> categories = product.getCategories(); // Assuming categories is a List<String>

        return productRepository.findByCategoryIn(categories); // Repository method that accepts a list of categories
    }
}
