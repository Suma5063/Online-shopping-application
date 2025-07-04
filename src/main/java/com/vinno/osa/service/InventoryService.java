package com.vinno.osa.service;

import com.vinno.osa.entity.Orders;
import com.vinno.osa.entity.Product;
import com.vinno.osa.repository.OrderRepository;
import com.vinno.osa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InventoryService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void updateInventoryOnReturn(String productId, Integer returnedQuantity) {
        // Fetch product or throw an exception if not found
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Get stock details (ensure it's not null)
        Product.StockDetails stock = product.getStockDetails();  // No need for Product.StockDetails
        if (stock == null) {
            stock = new Product.StockDetails(); // Corrected: Direct instantiation
        }

        stock.setAvailableStock(stock.getAvailableStock() + returnedQuantity);       // Update available stock and reserved stock
        stock.setReservedStock(Math.max(0, stock.getReservedStock() - returnedQuantity)); // Prevent negative values

        product.setStockDetails(stock);         // Save the updated stock details
        productRepository.save(product);
    }

    public Product purchaseProductAndUpdateStock(String productId, int purchaseQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Product.StockDetails stockDetails = product.getStockDetails();  // Use correct type
        if (stockDetails == null) {
            throw new RuntimeException("Stock details not found for product: " + product.getName());
        }
        // Check stock availability
        if (stockDetails.getAvailableStock() < purchaseQuantity) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
        }
        stockDetails.setAvailableStock(stockDetails.getAvailableStock() - purchaseQuantity);  // Update stock levels
        stockDetails.setReservedStock(stockDetails.getReservedStock() + purchaseQuantity);

        product.setStockDetails(stockDetails);   // Save updated stock details back to product
        productRepository.save(product);  // Persist updated product

        return product;

    }

    public Product purchaseProductAndUpdateStock(String productId, Integer quantity, String customerId) {
        // Step 1: Fetch the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Step 2: Check if there's enough stock
        if (product.getStockDetails().getAvailableStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        // Step 3: Update the stock
        product.getStockDetails().setAvailableStock(product.getStockDetails().getAvailableStock() - quantity);
        productRepository.save(product);

        // Step 4: Calculate total price
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        // Step 5: Save the order in the database
        Orders order = new Orders(productId, customerId, quantity, totalPrice, java.time.LocalDate.now().toString(), "Completed");
        orderRepository.save(order);

        // Step 6: Return the updated product
        return product;
    }

}
