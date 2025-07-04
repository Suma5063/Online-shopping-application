package com.vinno.osa.controller;

import com.vinno.osa.entity.Product;
import com.vinno.osa.entity.Wishlist;
import com.vinno.osa.service.JwtService;
import com.vinno.osa.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private JwtService jwtUtil;

    @PreAuthorize("hasAnyRole('BUYER', 'ADMIN')")
    @GetMapping("/view")
    public ResponseEntity<Wishlist> viewWishlist(@RequestHeader("Authorization") String token) {
        String userEmail = extractUserEmail(token);
        Wishlist wishlist = wishlistService.getWishlist(userEmail);
        return ResponseEntity.ok(wishlist);
    }

    @PreAuthorize("hasRole('BUYER')")
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromWishlist(
            @RequestParam String productId,
            @RequestHeader("Authorization") String token) {

        String userEmail = extractUserEmail(token);
        wishlistService.removeFromWishlist(userEmail, productId);
        return ResponseEntity.ok("Product removed from wishlist");
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/recommendations")
    public ResponseEntity<List<Product>> getWishlistRecommendations(@RequestHeader("Authorization") String token) {
        String userEmail = extractUserEmail(token);
        List<Product> recommendedProducts = wishlistService.getWishlistRecommendations(userEmail);
        return ResponseEntity.ok(recommendedProducts);
    }

    // Helper method to extract email from JWT token
    private String extractUserEmail(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return jwtUtil.extractUsername(token.substring(7)); // Remove "Bearer " prefix
    }

//    @PreAuthorize("hasRole('BUYER')")
//    @PostMapping("/add")
//    public ResponseEntity<Wishlist> addToWishlist(@RequestParam String userId, @RequestParam String productId) {
//        Wishlist wishlist = wishlistService.addToWishlist(userId, productId);
//        return ResponseEntity.ok(wishlist);
//    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/add")
    public ResponseEntity<Wishlist> addToWishlist(@RequestParam String productId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // Extract email from JWT

        Wishlist wishlist = wishlistService.addToWishlist(userEmail, productId);
        return ResponseEntity.ok(wishlist);
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/move-all-to-cart")
    public ResponseEntity<String> moveWishlistToCart() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // Extract user email from JWT token

        try {
            wishlistService.moveWishlistToCart(userEmail);
            return ResponseEntity.ok("All wishlist items moved to cart successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


}
