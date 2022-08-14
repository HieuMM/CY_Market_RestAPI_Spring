package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateQuestionDTO {
    private Long id;
    private String title;
    private String content;
    private List<Long> oldImageIds;
    private MultipartFile img[];
    private Long userId;
    private Long categoryId;
}
