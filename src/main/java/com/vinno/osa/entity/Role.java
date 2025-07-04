package com.vinno.osa.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Roles")
public class Role {

    private User user;

    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Role() {
    }
}
