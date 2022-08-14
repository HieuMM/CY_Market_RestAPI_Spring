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
public class UpdateBannerDTO {
    private Long id;
    private MultipartFile image;
    private String imageString;

    public static UpdateBannerDTO entityToDTO(BannerEntity object){
        return UpdateBannerDTO.builder()
                .id(object.getId())
                .build();
    }

    public static BannerEntity dtoToEntity(UpdateBannerDTO object){
        return BannerEntity.builder()
                .id(object.getId())
                .build();
    }
}
