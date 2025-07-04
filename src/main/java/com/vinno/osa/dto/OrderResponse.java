package com.vinno.osa.dto;

import com.vinno.osa.entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponse {

   // private String id;
   // private String userId;
    private String email;
    private String shippingAddress;
    private String deliveryDate;
    private String trackingLink;
    private List<OrderItem> items;
    private BigDecimal totalPrice;
    private String paymentStatus;
    private String returnStatus;
    private String returnReason;
    private boolean partialReturnAllowed;
}
