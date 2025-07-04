package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "Products")
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private List<String> images;  // URLs or filenames for images
    private String brand;
    private BigDecimal weight;
    private boolean available;
    private String category;
    private List<String> categories;
    private List<Review> reviews = new ArrayList<>();
    private List<ProductRecommendation> recommendations; // Cross-sell & upsell recommendations
    private String specifications; // Additional specifications about the product
    private List<String> videos; // List of video URLs for the product
    private Boolean isAvailable; // Availability status
    private String shippingPolicy; // Shipping details
    private String returnPolicy; // Return policy details
    private double rating;
    private double averageRating;  // Store the average rating
    private int totalReviews;      // Store total review count
    private boolean trending;

    private StockDetails stockDetails;

    public Product() {
    }

    // Methods to update review stats
    public void addReview(Review review) {
        reviews.add(review);
        calculateAverageRating();
    }

    public void calculateAverageRating() {
        this.totalReviews = reviews.size();
        this.averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    @Data
    public static class ProductRecommendation {
        private String productId;
        private String productName;

    }

    @Data
    public static class ProductStock {
        private int availableStock;
        private int reservedStock;

    }


    @Data
    public static class StockDetails {
        private int availableStock;
        private int reservedStock;

        // Getters and Setters
    }
}
