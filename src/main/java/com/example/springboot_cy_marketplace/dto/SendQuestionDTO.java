package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SendQuestionDTO {
    private String title;
    private String content;
    private MultipartFile img[];
    private Long userId;
    private Long categoryId;
}
