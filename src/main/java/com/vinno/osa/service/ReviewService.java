package com.vinno.osa.service;
import com.vinno.osa.entity.Product;
import com.vinno.osa.entity.Review;
import com.vinno.osa.entity.User;
import com.vinno.osa.repository.ProductRepository;
import com.vinno.osa.repository.ReviewRepository;
import com.vinno.osa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Review addReview(String productId, int rating, String comment) {
        // Extract the email from the JWT token
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Create a new Review object
        Review review = new Review();
        review.setUserId(userEmail);  // Use email as userId
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setDate(new Date());

        reviewRepository.save(review);           // Save the review in the review collection

        // Add the review to the product's list of reviews
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getReviews() == null) {
            product.setReviews(new ArrayList<>());
        }
        product.getReviews().add(review);  // Add Review to the list
        productRepository.save(product);

        // Add the review to the user's list of reviews
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getReviews() == null) {
            user.setReviews(new ArrayList<>());
        }
        user.getReviews().add(review);  // Assuming user has a reviews field
        userRepository.save(user);

        return review;
    }

    // Method to get all reviews for a product by productId
    public List<Review> getReviewsByProductId(String productId) {
        // Fetch product to ensure it exists (optional step)
        productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Retrieve all reviews for the given productId
        return reviewRepository.findByProductId(productId);
    }

}
