package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.QuestionEntity;
import com.example.springboot_cy_marketplace.model.QuestionModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class QuestionDTO {
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
    private String avatarUrl;
    private Long categoryId;
    private String categoryName;

    public QuestionDTO() {
    }

    public QuestionDTO(Long id, String title, String content, String img1, String img2, String img3, Long userId,
                       String status, String createdDate, String createdBy, String modifiedBy, String modifiedDate,
                       Long categoryId, String userFullName, String userEmail, String avatarUrl, String categoryName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedDate = modifiedDate;
        this.modifiedBy = modifiedBy;
        this.status = status;
        this.avatarUrl = avatarUrl;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public static QuestionDTO toDto(QuestionEntity entity) {
        return QuestionDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .img1(entity.getImg1())
                .img2(entity.getImg2())
                .img3(entity.getImg3())
                .userId(entity.getUserEntity().getId())
                .userFullName(entity.getUserEntity().getFullName())
                .userEmail(entity.getUserEntity().getEmail())
                .createdDate(entity.getCreateDate().toString())
                .createdBy(entity.getCreateBy())
                .modifiedDate(entity.getModifiedDate().toString())
                .modifiedBy(entity.getModifiedBy())
                .status(entity.getStatus())
                .avatarUrl(entity.getUserEntity().getAvatar())
                .categoryId(entity.getQuestionCategory().getId())
                .categoryName(entity.getQuestionCategory().getName())
                .build();
    }

    public static QuestionModel dtoToModel(QuestionDTO dto) {
        QuestionModel model = new QuestionModel();
        model.setId(dto.getId());
        model.setTitle(dto.getTitle());
        model.setContent(dto.getContent());
        model.setUserId(dto.getUserId());
        model.setUserFullName(dto.getUserFullName());
        model.setUserEmail(dto.getUserEmail());
        model.setCreatedDate(dto.getCreatedDate());
        model.setCreatedBy(dto.getCreatedBy());
        model.setModifiedDate(dto.getModifiedDate());
        model.setModifiedBy(dto.getModifiedBy());
        model.setStatus(dto.getStatus());
        model.setAvatarUrl(dto.getAvatarUrl());
        model.setCategoryId(dto.getCategoryId());
        model.setCategoryName(dto.getCategoryName());

        if (dto.getImg1() != null) {
            model.setImg1(dto.getImg1());
        } else {
            model.setImg1("");
        }
        if (dto.getImg2() != null) {
            model.setImg2(dto.getImg2());
        } else {
            model.setImg2("");
        }
        if (dto.getImg3() != null) {
            model.setImg3(dto.getImg3());
        } else {
            model.setImg3("");
        }
        return model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
