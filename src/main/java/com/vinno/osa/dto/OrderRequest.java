package com.vinno.osa.dto;
import com.vinno.osa.entity.Address;
import com.vinno.osa.entity.OrderItem;
import com.vinno.osa.entity.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequest {

    private String userId;
    private String email;
    private String orderId;
    private List<OrderItem> items;
    private BigDecimal totalPrice;
    private Address shippingAddress;
    private String trackingLink;
    private String deliveryDate;

    private String couponCode;

    private PaymentStatus paymentStatus;
    private List<OrderItem> cartItems;

    public OrderRequest() {
    }
}
