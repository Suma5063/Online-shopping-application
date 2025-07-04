package com.vinno.osa.service;
import com.vinno.osa.entity.*;
import com.vinno.osa.repository.CartRepository;
import com.vinno.osa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;


    public Cart getCartByUserId(String userId) {
        // Retrieve the cart by userId, handle case if cart doesn't exist
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
    }

    public Cart getCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    private List<OrderItem> convertCartToOrderItems(Cart cart) {
        return cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setProductName(cartItem.getProductName());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    return orderItem;
                })
                .collect(Collectors.toList());
    }

    public Cart addToCart(CartItem cartItem, String userEmail) {
        // Find existing cart for user
        Cart cart = cartRepository.findByUserId(userEmail)
                .orElseGet(() -> new Cart(userEmail));  // Create new cart if not found

        cart.setUserId(userEmail);

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(cartItem.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update the quantity if product exists in cart
            existingItem.get().setQuantity(existingItem.get().getQuantity() + cartItem.getQuantity());
        } else {
            // Fetch product details from the database
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            cartItem.setProductName(product.getName());
            cartItem.setPrice(product.getPrice());
            cart.getCartItems().add(cartItem);
        }

        cart.setTotalPrice(calculateTotalPrice(cart).doubleValue());

        return cartRepository.save(cart);       // Save the updated cart
    }


    public Cart getCartByUser(String userEmail) {
        return cartRepository.findByUserId(userEmail)
                .orElse(new Cart(userEmail));
    }


    public Cart removeProductFromCart(String userId, String productId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        // Find and remove the product in the cart
        boolean removed = cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
        if (!removed) {
            throw new RuntimeException("Product not found in cart: " + productId);
        }
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(totalPrice.doubleValue());

        // If cart is empty after removal, delete it
        if (cart.getCartItems().isEmpty()) {
            cartRepository.delete(cart);
            return null; // Return null if cart is deleted
        }
        cart.setUpdatedAt(LocalDateTime.now());

        return cartRepository.save(cart);      // Save and return updated cart
    }

    public Cart updateCartItem(String userId, String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // Find cart by user ID
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        // Find the item in the cart
        Optional<CartItem> cartItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(quantity); // Update quantity
        } else {
            throw new RuntimeException("Product not found in cart");
        }
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice.doubleValue());

        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        // Find cart by user ID
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        cart.getCartItems().clear();        // Remove all cart items
        cart.setTotalPrice(0.0);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }

    public Cart getCartItems(String userId) {
        // Find cart by user ID
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
    }

    private BigDecimal calculateTotalPrice(Cart cart) {
        return cart.getCartItems().stream()
                .map(item ->item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

