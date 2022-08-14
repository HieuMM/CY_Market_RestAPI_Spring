package com.example.springboot_cy_marketplace.jwt.payload.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private Long id;
    private String email;
    private String accessToken;
    private String tokenType ="Bearer";
    private boolean rememberme;
    private List<String> role;

}
