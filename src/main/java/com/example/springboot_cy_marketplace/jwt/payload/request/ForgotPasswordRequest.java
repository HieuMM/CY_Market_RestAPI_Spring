package com.example.springboot_cy_marketplace.jwt.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ForgotPasswordRequest {
    private long id;
    private String title;
    private String content;
    private String timeRequest;
    private boolean status;
}
