package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.dto.QuestionDTO;
import com.example.springboot_cy_marketplace.entity.QuestionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class này đại diện cho lớp thứ 2 trong mô hình DTO - Model - Entity
 * This class stands for 2nd class in diagram DTO - Model - Entity
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class QuestionModel {
    private Long id;
    private String title;
    private String content;
    private String img1;
    private String img2;
    private String img3;
    private Long userId;
    private String userFullName;
    private String userEmail;
    private String createdDate;
    private String createdBy;
    private String modifiedDate;
    private String modifiedBy;
    private String status;
    private Long categoryId;
    private String avatarUrl;
    private String categoryName;

    public static QuestionModel entityToModel(QuestionEntity object){
        return QuestionModel.builder()
                .id(object.getId())
                .title(object.getTitle())
                .content(object.getContent())
                .img1(object.getImg1())
                .img2(object.getImg2())
                .img3(object.getImg3())
                .userId(object.getUserEntity().getId())
                .userFullName(object.getUserEntity().getFullName())
                .userEmail(object.getUserEntity().getEmail())
                .createdDate(object.getCreateDate().toString())
                .createdBy(object.getCreateBy())
                .modifiedDate(object.getModifiedDate().toString())
                .modifiedBy(object.getModifiedBy())
                .status(object.getStatus())
                .avatarUrl(object.getUserEntity().getAvatar())
                .categoryId(object.getQuestionCategory().getId())
                .categoryName(object.getQuestionCategory().getName())
                .build();
    }

    public static QuestionDTO modelToDTO(QuestionModel object){
        return QuestionDTO.builder()
                .id(object.getId())
                .title(object.getTitle())
                .content(object.getContent())
                .img1(object.getImg1())
                .img2(object.getImg2())
                .img3(object.getImg3())
                .userId(object.getUserId())
                .userFullName(object.getUserFullName())
                .userEmail(object.getUserEmail())
                .createdDate(object.getCreatedDate())
                .createdBy(object.getCreatedBy())
                .modifiedDate(object.getModifiedDate())
                .modifiedBy(object.getModifiedBy())
                .status(object.getStatus())
                .avatarUrl(object.getAvatarUrl())
                .categoryId(object.getCategoryId())
                .categoryName(object.getCategoryName())
                .build();
    }
}
