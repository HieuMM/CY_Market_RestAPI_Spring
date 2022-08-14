package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.BannerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerModel {
    private Long id;
    private String name;
    private String image;
    private String link;

    public static BannerModel entityToModel(BannerEntity object){
        return BannerModel.builder()
                .id(object.getId())
                .name(object.getName())
                .image(object.getImage())
                .link(object.getLink())
                .build();
    }
}
