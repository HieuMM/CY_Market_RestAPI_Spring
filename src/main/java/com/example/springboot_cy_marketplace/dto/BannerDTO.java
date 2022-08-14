package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.BannerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO {
    private Long id;
    private String name;
    private String link;
    private MultipartFile image;
    private String imageString;

    public static BannerDTO entityToDTO(BannerEntity object){
        return BannerDTO.builder()
                .id(object.getId())
                .name(object.getName())
                .imageString(object.getImage())
                .link(object.getLink())
                .build();
    }

    public static BannerEntity dtoToEntity(BannerDTO object){
        return BannerEntity.builder()
                .id(object.getId())
                .name(object.getName())
                .link(object.getLink())
                .build();
    }
}
