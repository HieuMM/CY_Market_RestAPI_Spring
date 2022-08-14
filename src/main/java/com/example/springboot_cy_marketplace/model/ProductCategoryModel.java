package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.dto.ProductCategoryDTO;
import com.example.springboot_cy_marketplace.entity.ProductCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductCategoryModel {
    private Long id;
    private String name;
    private String avatarUrl;
    private Long parentId;
    private String parentName;
    private String parentAvatarUrl;

    public static ProductCategoryDTO modelToDTO(ProductCategoryModel model){
        ProductCategoryDTO dto = ProductCategoryDTO.builder()
                .id(model.getId())
                .name(model.getName())
                .avatarUrl(model.getAvatarUrl())
                .parentId(model.getParentId())
                .parentAvatarUrl(model.getParentAvatarUrl())
                .build();
        return dto;
    }

    public static ProductCategoryModel entityToModel(ProductCategoryEntity object){
        return ProductCategoryModel.builder()
                .id(object.getId())
                .name(object.getName())
                .avatarUrl(object.getAvatarUrl())
                .parentId(object.getParentCategory() != null ? object.getParentCategory().getId() : null)
                .parentName(object.getParentCategory() != null ? object.getParentCategory().getName() : null)
                .parentAvatarUrl(object.getParentCategory() != null ? object.getParentCategory().getAvatarUrl() : null)
                .build();
    }

}
