package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.AnswerEntity;
import com.example.springboot_cy_marketplace.model.AnswerModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTO {
    private Long id;
    private String content;
    private Long idQuestion;
    private UserDTO userDTO;
    private String createBy;
    private Date createDate;
    private String modifiedBy;
    private Date modifiedDate;

    public static AnswerModel dtoToModel(AnswerDTO object){
        return AnswerModel.builder()
                .id(object.getId())
                .content(object.getContent())
                .idQuestion(object.getIdQuestion())
                .idUser(object.getUserDTO().getId())
                .createBy(object.getCreateBy())
                .createDate(object.getCreateDate())
                .modifiedBy(object.getModifiedBy())
                .modifiedDate(object.getModifiedDate())
                .build();

    }
    public static AnswerEntity dtoToEntity(AnswerDTO object){
        return AnswerEntity.builder()
                .id(object.getId())
                .content(object.getContent())
                .userEntity(UserDTO.dtoToEntity(object.getUserDTO()))
                .build();

    }
}
