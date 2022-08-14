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
public class UpdateAvatarProfileRequest {
    private Long id;
    private MultipartFile avatarFile;
}
