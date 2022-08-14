package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.ProductClassifiedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductClassifiedModel {
    private Long id;
    private String classifyName1;
    private String classifyName2;
    private String newPrice;
    private String oldPrice;
    private int discount;
    private int amount;
    private String image;
    private Long idProduct;

    public static ProductClassifiedEntity modelToEntity(ProductClassifiedModel object){
        return ProductClassifiedEntity.builder()
                .id(object.getId())
                .classifyName1(object.getClassifyName1())
                .classifyName2(object.getClassifyName2())
                .newPrice(object.getNewPrice())
                .oldPrice(object.getOldPrice())
                .discount(object.getDiscount())
                .amount(object.getAmount())
                .image(object.getImage())
                .build();
    }

    public static ProductClassifiedModel entityToModel(ProductClassifiedEntity object){
        return ProductClassifiedModel.builder()
                .id(object.getId())
                .classifyName1(object.getClassifyName1())
                .classifyName2(object.getClassifyName2())
                .newPrice(object.getNewPrice())
                .oldPrice(object.getOldPrice())
                .discount(object.getDiscount())
                .amount(object.getAmount())
                .image(object.getImage())
                .idProduct(object.getProductEntity().getId())
                .build();
    }
}
