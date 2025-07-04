package com.vinno.osa.dto;

import com.vinno.osa.entity.Address;
import lombok.Data;

@Data
public class RegisterResponse {

    private String userId;
    private String username;
    private String name;
    private String email;
    private String roleName;
    private Address address;
    private String message;

    public RegisterResponse(String msg) {
        this.message=msg;
    }

    public RegisterResponse(String userId, String username, String name, String email, String roleName, Address address, String message) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.roleName = roleName;
        this.address = address;
        this.message = message;
    }
}
