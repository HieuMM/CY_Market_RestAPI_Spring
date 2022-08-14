package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.QuestionCategoryEntity;
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
@Data
@Builder
public class QuestionCategoryModel {
    private Long id;
    private String name;
    private String description;

    public static QuestionCategoryModel entityToModel(QuestionCategoryEntity entity){
        QuestionCategoryModel model = new QuestionCategoryModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        return model;
    }
}
