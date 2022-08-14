package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.dto.NoticesLocalDTO;
import com.example.springboot_cy_marketplace.entity.NoticesLocalEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NoticesLocalModel {
    private Long id;
    private String title;
    private String content;
    private String img1;
    private String img2;
    private String img3;
    private String createBy;
    private Date createDate;
    private Long idUser;
    private NotifyCategoryModel notifyCategoryModel;

    public static NoticesLocalModel entityToModel(NoticesLocalEntity object){
        return NoticesLocalModel.builder()
                .id(object.getId())
                .title(object.getTitle())
                .content(object.getContent())
                .img1(object.getImg1())
                .img2(object.getImg2())
                .img3(object.getImg3())
                .createBy(object.getCreateBy())
                .createDate(object.getCreateDate())
                .idUser(object.getUserEntity() != null ? object.getUserEntity().getId() : null)
                .notifyCategoryModel(object.getNotifyCategoryEntity() != null ? NotifyCategoryModel.entityToModel(object.getNotifyCategoryEntity()) : null)
                .build();
    }

    public static NoticesLocalEntity modelToEntity(NoticesLocalModel object){
        return NoticesLocalEntity.builder()
                .id(object.getId())
                .title(object.getTitle())
                .content(object.getContent())
                .img1(object.getImg1())
                .img2(object.getImg2())
                .img3(object.getImg3())
                .build();
    }

    public static NoticesLocalDTO modelToDTO(NoticesLocalModel object){
        return NoticesLocalDTO.builder()
                .id(object.getId())
                .title(object.getTitle())
                .content(object.getContent())
                .img1(object.getImg1())
                .img2(object.getImg2())
                .img3(object.getImg3())
                .createBy(object.getCreateBy())
                .createDate(object.getCreateDate())
                .build();
    }

}
