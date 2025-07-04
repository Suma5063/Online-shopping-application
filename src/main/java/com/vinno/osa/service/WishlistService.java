package com.vinno.osa.service;

import com.vinno.osa.entity.*;
import com.vinno.osa.exception.WishlistNotFoundException;
import com.vinno.osa.repository.CartRepository;
import com.vinno.osa.repository.ProductRepository;
import com.vinno.osa.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public Wishlist addToWishlist(String userId, String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElse(new Wishlist());

        wishlist.setUserId(userId);
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setProductId(product.getId());
        wishlistItem.setProductName(product.getName());
        wishlistItem.setPrice(product.getPrice());

        if (wishlist.getItems() == null) {
            wishlist.setItems(new ArrayList<>());
        }
        wishlist.setCreatedAt(LocalDateTime.now());
        wishlist.setUpdatedAt(LocalDateTime.now());
        wishlist.getItems().add(wishlistItem);
        return wishlistRepository.save(wishlist);
    }

    private BigDecimal calculateTotalPrice(Cart cart) {
        return cart.getCartItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public Wishlist getWishlist(String userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));
    }

    public void removeFromWishlist(String userId, String productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));
        wishlist.getItems().removeIf(item -> item.getProductId().equals(productId));
        wishlistRepository.save(wishlist);
    }

    public List<Product> getWishlistRecommendations(String userId) {
        // Get user's wishlist product IDs
        List<String> wishlistProductIds = wishlistRepository.findByUserId(userId)
                .stream()
                .map(Wishlist::getId)
                .collect(Collectors.toList());

        // Find wishlist products that are on sale or low in stock
        return productRepository.findByIdInAndOnSaleOrLowStock(wishlistProductIds);
    }

    public void moveWishlistToCart(String userId) {
        // Fetch wishlist for the user
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new WishlistNotFoundException("Wishlist not found for user " + userId));

        // Check if the wishlist is empty
        if (wishlist.getItems().isEmpty()) {
            throw new WishlistNotFoundException("Wishlist is empty. Nothing to move.");
        }

        Cart cart = cartRepository.findByUserId(userId).orElse(null);       // Fetch or create a cart for the user

        if (cart == null) {
            // Create a new cart for the user if not found
            cart = new Cart(userId);
            cart.setTotalPrice(0.0);
            cartRepository.save(cart); // Save the new cart
            System.out.println("Cart created for user: " + userId); // Debugging line
        }

        // Move all items from the wishlist to the cart
        for (WishlistItem wishlistItem : wishlist.getItems()) {
            // Check if the item already exists in the cart
            CartItem existingCartItem = cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getProductId().equals(wishlistItem.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (existingCartItem != null) {
                // If the item exists in the cart, increase the quantity
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            } else {
                // Otherwise, add the item as a new cart item
                CartItem cartItem = new CartItem();
                cartItem.setProductId(wishlistItem.getProductId());
                cartItem.setProductName(wishlistItem.getProductName());
                cartItem.setPrice(wishlistItem.getPrice());
                cartItem.setQuantity(1); // Default quantity

                cart.getCartItems().add(cartItem); // Add new cart item
            }
        }

        //wishlist.getItems().clear();                                   // Clear the wishlist after moving items to the cart
        wishlistRepository.save(wishlist);
        cart.setTotalPrice(calculateTotalPrice(cart).doubleValue());     // Update the total price of the cart

        cartRepository.save(cart);                                       // Save the updated cart
    }

}
