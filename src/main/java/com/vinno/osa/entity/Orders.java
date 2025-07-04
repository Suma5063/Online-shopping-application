package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "orders")
public class Orders {

    @Id
    private String id;

    private String userId;

    private String email;
    private Address shippingAddress;

    private List<OrderItem> items;
    private OrderStatus status;
    private String deliveryDate = "";

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private double totalPrice;

    private PaymentStatus paymentStatus;

    private boolean returnProcessed = false; // Default is false
    private String returnStatus="PENDING"; // "Requested", "Approved", "Refunded", etc.
    private String returnReason; // Reason for the return (optional)
    private boolean partialReturnAllowed; // If partial return is allowed
    private LocalDateTime orderDate;

    public Orders() {
    }


    public Orders(String productId, String customerId, Integer quantity, BigDecimal totalPrice, String string, String completed) {
    }
}
