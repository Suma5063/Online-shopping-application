package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "Cart")
public class Cart
{

    @Id
    private String id;

    private String userId;

    private Double totalPrice;

    private List<CartItem> cartItems = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Cart() {
    }

    public Cart(String userId, ArrayList<Object> objects, BigDecimal zero) {
    }

    public Cart(String userEmail) {

    }
}
