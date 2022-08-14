package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.NoticesLocalEntity;
import com.example.springboot_cy_marketplace.model.NoticesLocalModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NoticesLocalDTO {
    private Long id;
    private String title;
    private String content;
    private String img1;
    private String img2;
    private String img3;
    private String createBy;
    private Date createDate;
    private Long idUser;
    private NotifyCategoryDTO notifyCategoryDTO;

    public static NoticesLocalDTO entityToDTO(NoticesLocalModel object){
        return NoticesLocalDTO.builder()
                .id(object.getId())
                .title(object.getTitle())
                .content(object.getContent())
                .img1(object.getImg1())
                .img2(object.getImg2())
                .img3(object.getImg3())
                .createBy(object.getCreateBy())
                .createDate(object.getCreateDate())
                .idUser(object.getIdUser() != null ? object.getIdUser() : null)
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
                .idUser(object.getIdUser() != null ? object.getIdUser() : null)
                .notifyCategoryDTO(object.getNotifyCategoryModel() != null ? NotifyCategoryDTO.modelToDTO(object.getNotifyCategoryModel()) : null)
                .build();
    }
    public static NoticesLocalEntity dtoToEntity(NoticesLocalDTO object){
        return NoticesLocalEntity.builder()
                .id(object.getId())
                .title(object.getTitle())
                .content(object.getContent())
                .img1(object.getImg1())
                .img2(object.getImg2())
                .img3(object.getImg3())
                .build();
    }


}
