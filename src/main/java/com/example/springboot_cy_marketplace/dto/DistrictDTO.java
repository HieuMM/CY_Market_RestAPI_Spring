package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.CityEntity;
import com.example.springboot_cy_marketplace.entity.DistrictEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DistrictDTO {
    private Long id;
    private String name;
    private String type;
    private CityEntity city;
    private Long idGHN;
    public static DistrictDTO toDto(DistrictEntity entity) {
        return DistrictDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .idGHN(entity.getIdGHN())
                .build();
    }
}
