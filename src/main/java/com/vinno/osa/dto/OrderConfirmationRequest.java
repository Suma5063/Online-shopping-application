package com.vinno.osa.dto;

import com.vinno.osa.entity.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderConfirmationRequest {

    private String userEmail;
    private String orderId;
    private List<OrderItem> items; // Use a list instead of String
    private double totalCost;
    private String shippingAddress;
    private String trackingLink;
}
