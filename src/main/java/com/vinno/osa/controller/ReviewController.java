package com.vinno.osa.controller;
import com.vinno.osa.entity.Review;
import com.vinno.osa.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/add")
    public ResponseEntity<Review> addReview(
            @RequestHeader("Authorization") String token,
            @RequestParam String productId,
            @RequestParam int rating,
            @RequestParam String comment) {

        // Remove "Bearer " prefix and process the review
        Review review = reviewService.addReview(productId, rating, comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("/{productId}")
    public List<Review> getReviewsByProductId(@PathVariable String productId) {
        return reviewService.getReviewsByProductId(productId);
    }
}
