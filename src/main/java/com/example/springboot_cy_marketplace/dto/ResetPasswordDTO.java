package com.example.springboot_cy_marketplace.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String email;
    private String token;
    private String password;
    private String confirmPass;
}
