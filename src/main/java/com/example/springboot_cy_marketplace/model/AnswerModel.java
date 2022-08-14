package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.AnswerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerModel {
    private Long id;
    private String content;
    private Long idQuestion;
    private Long idUser;
    private String createBy;
    private Date createDate;
    private String modifiedBy;
    private Date modifiedDate;


    public static AnswerModel entityToModel(AnswerEntity object){
        return AnswerModel.builder()
                .id(object.getId())
                .content(object.getContent())
                .idQuestion(object.getQuestionEntity().getId())
                .idUser(object.getUserEntity().getId())
                .createBy(object.getCreateBy())
                .createDate(object.getCreateDate())
                .modifiedBy(object.getModifiedBy())
                .modifiedDate(object.getModifiedDate())
                .build();
    }

}
