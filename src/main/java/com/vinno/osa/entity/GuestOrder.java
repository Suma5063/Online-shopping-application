package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "GuestOrder")
public class GuestOrder {

    @Id
    private String id;
    private String email;
    private List<OrderItem> items;
    private double totalPrice;
    private String status;

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GuestOrder() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
