package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.model.NotifyCategoryModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyCategoryDTO {
    private Long id;
    private String name;
    private String defaultUrl;

    public static NotifyCategoryDTO modelToDTO(NotifyCategoryModel object){
        return NotifyCategoryDTO.builder()
                .id(object.getId())
                .name(object.getName())
                .defaultUrl(object.getDefaultUrl())
                .build();
    }
}
