package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.DistrictEntity;
import com.example.springboot_cy_marketplace.entity.WardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WardDTO {
    private Long id;
    private String name;
    private String type;
    private DistrictEntity district;
    public static WardDTO toDto(WardEntity entity) {
        return WardDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .build();
    }
}
