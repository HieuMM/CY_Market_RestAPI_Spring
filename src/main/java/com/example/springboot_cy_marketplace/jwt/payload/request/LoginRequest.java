package com.example.springboot_cy_marketplace.jwt.payload.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
    private boolean rememberme;
}
