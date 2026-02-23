package com.embarkx.FirstSpring.dto;

import lombok.Data;
import com.embarkx.FirstSpring.model.UserRole;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO address;
}
