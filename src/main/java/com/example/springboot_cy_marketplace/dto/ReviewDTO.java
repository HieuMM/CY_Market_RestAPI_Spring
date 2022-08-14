package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.ReviewEntity;
import com.example.springboot_cy_marketplace.model.ReviewModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private int rate;
    private MultipartFile image;
    private String reviewtext;
    private Long productId;
    private Long userId;
    private Long orderId;
/*
* @author: HieuMM
* @since: 10-Jun-22 11:35 AM
* @description:convert ReviewEntity to ReviewDTO
* @update:
* */
    public static ReviewModel entityToDto(ReviewEntity object){
        return ReviewModel.builder()
                .id(object.getId())
                .rate(object.getRate())
                .image(object.getImage())
                .reviewtext(object.getReviewtext())
                .productId(object.getProduct().getId())
                .userId(object.getUserEntity().getId())
                .orderId(object.getOrderEntity().getId())
                .build();
    }
}
