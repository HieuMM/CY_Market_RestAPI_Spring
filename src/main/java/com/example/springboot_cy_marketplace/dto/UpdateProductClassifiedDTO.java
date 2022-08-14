package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.ProductClassifiedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductClassifiedDTO {
    private Long id;
    private String classifyName1;
    private String classifyName2;
    private String newPrice;
    private String oldPrice;
    private int discount;
    private int amount;
    private MultipartFile image;
    private String imageString;
    private Long idProduct;

    public static UpdateProductClassifiedDTO entityToDTO(ProductClassifiedEntity object){
        return UpdateProductClassifiedDTO.builder()
                .id(object.getId())
                .classifyName1(object.getClassifyName1())
                .classifyName2(object.getClassifyName2())
                .newPrice(object.getNewPrice())
                .oldPrice(object.getOldPrice())
                .discount(object.getDiscount())
                .amount(object.getAmount())
                .imageString(object.getImage())
                .idProduct(object.getProductEntity().getId())
                .build();
    }

    public static ProductClassifiedEntity dtoToEnitty(UpdateProductClassifiedDTO object){
        return ProductClassifiedEntity.builder()
                .id(object.getId())
                .classifyName1(object.getClassifyName1())
                .classifyName2(object.getClassifyName2())
                .newPrice(object.getNewPrice())
                .oldPrice(object.getOldPrice())
                .discount(object.getDiscount())
                .amount(object.getAmount())
                .image(object.getImageString())
                .build();
    }
}
