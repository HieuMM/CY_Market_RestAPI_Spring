package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.ProductCategoryEntity;
import com.example.springboot_cy_marketplace.model.ProductCategoryModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductCategoryDTO {
    private Long id;
    private String name;
    private String avatarUrl;
    private Long parentId;
    private String parentName;
    private String parentAvatarUrl;

    public static ProductCategoryModel dtoToModel(ProductCategoryDTO dto){
        ProductCategoryModel model = ProductCategoryModel.builder()
                .id(dto.getId())
                .name(dto.getName())
                .avatarUrl(dto.getAvatarUrl())
                .parentId(dto.getParentId())
                .parentName(dto.getParentName())
                .parentAvatarUrl(dto.getParentAvatarUrl())
                .build();
        return model;
    }

    public static ProductCategoryDTO entityToDTO(ProductCategoryEntity object){
        return ProductCategoryDTO.builder()
                .id(object.getId())
                .name(object.getName())
                .avatarUrl(object.getAvatarUrl())
                .parentId(object.getParentCategory() != null ? object.getParentCategory().getId() : null)
                .parentName(object.getParentCategory() != null ? object.getParentCategory().getName() : null)
                .parentAvatarUrl(object.getParentCategory() != null ? object.getParentCategory().getAvatarUrl() : null)
                .build();
    }
}
