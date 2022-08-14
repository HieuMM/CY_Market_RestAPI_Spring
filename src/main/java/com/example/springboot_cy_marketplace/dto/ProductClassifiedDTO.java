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
public class ProductClassifiedDTO {
    private Long id;
    private String classifyName1;
    private String classifyName2;
    private String newPrice;
    private String oldPrice;
    private int discount;
    private int amount;
    private MultipartFile image;
    private Long idProduct;

    public static ProductClassifiedEntity dtoToEntity(ProductClassifiedDTO object){
        return ProductClassifiedEntity.builder()
                .id(object.getId())
                .classifyName1(object.getClassifyName1())
                .classifyName2(object.getClassifyName2())
                .newPrice(object.getNewPrice())
                .oldPrice(object.getOldPrice())
                .discount(object.getDiscount())
                .amount(object.getAmount())
                .build();
    }
}
