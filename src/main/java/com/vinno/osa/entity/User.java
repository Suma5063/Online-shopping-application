package com.vinno.osa.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "Users")
public class User {

    @Id
    private ObjectId userId;
    private String name;
    private String username;
    private String password;
    private String email;
    private boolean isActive;
    private boolean emailVerified;
    private List<Address> addresses;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted=false;
    private Set<Role> role;

    private List<Role> userRoles;

    private Address address;

    private List<Review> reviews = new ArrayList<>();

    public User() {
    }

    public User(ObjectId userId, String name, String email, String password,Role role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.deleted = false;

    }

    public User(String email, String encode, Role role) {
        this.email=email;

    }

    public void setIsActive(boolean b) {
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }
}
