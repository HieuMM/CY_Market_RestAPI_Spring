package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.CityEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CityModel {
    private Long id;
    private String name;
    private String type;
    private String slug;
    private Long idGHN;

    public static CityModel entityToModel(CityEntity entity){
        CityModel model = new CityModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setType(entity.getType());
        model.setSlug(entity.getSlug());
        model.setIdGHN(entity.getIdGHN());
        return model;
    }
}
