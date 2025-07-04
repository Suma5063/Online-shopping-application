package com.vinno.osa.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String name;
    private String email;
    private String phone;
    private String password;  // Password (optional during update)

}
