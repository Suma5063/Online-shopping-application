package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "Wishlists")
public class Wishlist {

    @Id
    private String id;

    private String userId;

    private List<WishlistItem> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Wishlist() {
    }

    public Wishlist(String id, String userId, List<WishlistItem> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
