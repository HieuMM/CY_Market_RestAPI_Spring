package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddProductCategoryDTO {
    private String name;
    private String avatarUrl;
    private Long parentId;

    public static ProductCategoryDTO requestToDTO(AddProductCategoryDTO request){
        ProductCategoryDTO dto = new ProductCategoryDTO();
        dto.setName(request.getName());
        dto.setAvatarUrl(request.getAvatarUrl());
        dto.setParentId(request.getParentId());
        return dto;
    }
}
