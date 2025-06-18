package com.petproject.Securehub.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterUserRequest {
    private String username;
    private String password;
    private Set<String> roles;  // ["ROLE_USER"] or ["ROLE_USER", "ROLE_ADMIN"]
}