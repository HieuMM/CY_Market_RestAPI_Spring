package com.example.springboot_cy_marketplace.jwt.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class UpdateProfileRequest {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
}
