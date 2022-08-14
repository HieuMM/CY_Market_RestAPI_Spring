package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.CityEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CityDTO {
    private Long id;
    private String name;
    private String type;
    private String slug;
    private Long idGHN;
    public static CityDTO toDto(CityEntity entity) {
        return CityDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .slug(entity.getSlug())
                .idGHN(entity.getIdGHN())
                .build();
    }
}
