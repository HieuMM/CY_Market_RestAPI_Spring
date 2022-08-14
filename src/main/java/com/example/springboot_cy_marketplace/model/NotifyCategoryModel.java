package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.NotifyCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyCategoryModel {
    private Long id;
    private String name;
    private String defaultUrl;

    public static NotifyCategoryModel entityToModel(NotifyCategoryEntity object){
        return NotifyCategoryModel.builder()
                .id(object.getId())
                .name(object.getName())
                .defaultUrl(object.getDefaultUrl())
                .build();
    }
}
