package com.vinno.osa.controller;
import com.vinno.osa.entity.Cart;
import com.vinno.osa.entity.CartItem;
import com.vinno.osa.exception.CartNotFoundException;
import com.vinno.osa.repository.CartRepository;
import com.vinno.osa.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import static com.vinno.osa.config.AppConfig.getAuthenticatedUserEmail;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody CartItem cartItem) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // Get user email from JWT
        Cart updatedCart = cartService.addToCart(cartItem, userEmail);
        updatedCart.setCreatedAt(LocalDateTime.now());
        updatedCart.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(updatedCart);
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/view")
    public ResponseEntity<Cart> viewCart() {
        String userEmail = getAuthenticatedUserEmail();  // Get user email from JWT token

        Cart cart = cartRepository.findByUserId(userEmail)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user " + userEmail));

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('BUYER')")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable String productId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // Get user email from JWT

        Cart updatedCart = cartService.removeProductFromCart(userEmail, productId);

        if (updatedCart == null) {
            return ResponseEntity.ok("Cart is empty. It has been deleted.");
        }

        return ResponseEntity.ok(updatedCart);
    }

    @PreAuthorize("hasRole('BUYER')")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        String userEmail = getAuthenticatedUserEmail();  // Get user email from JWT token

        Cart cart = cartRepository.findByUserId(userEmail)
                .orElseThrow(() -> new RuntimeException("Cart not found for user " + userEmail));

        cart.getCartItems().clear();  // Clear all items in the cart
        cartRepository.save(cart);

        return new ResponseEntity<>("Cart cleared successfully", HttpStatus.OK);
    }

}