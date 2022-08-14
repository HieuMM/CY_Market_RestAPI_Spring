package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.ProductClassifiedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartProductDTO {
    private Long productId;
    private String productName;
    private String productCoverImage;
    private Long productClassifiedId;
    private String productClassifiedBy01;
    private String productClassifiedName1;
    private String productClassifiedBy02;
    private String productClassifiedName2;
    private String classifiedImage;
    private int height;
    private int width;
    private int length;
    private int weight;
    private Double newPrice;
    private Double oldPrice;
    private int discount;
    private int amount;
    private int quantity;

    public static CartProductDTO entityToDTO(ProductClassifiedEntity entity){
        CartProductDTO cartProductDTO = new CartProductDTO();
        cartProductDTO.setProductId(entity.getProductEntity().getId());
        cartProductDTO.setProductName(entity.getProductEntity().getName());
        cartProductDTO.setProductCoverImage(entity.getProductEntity().getCoverImage());
        cartProductDTO.setProductClassifiedId(entity.getId());
        cartProductDTO.setProductClassifiedName1(entity.getClassifyName1());
        cartProductDTO.setProductClassifiedName2(entity.getClassifyName2());
        cartProductDTO.setClassifiedImage(entity.getImage());
        cartProductDTO.setNewPrice(Double.parseDouble(entity.getNewPrice()));
        cartProductDTO.setOldPrice(Double.parseDouble(entity.getOldPrice()));
        cartProductDTO.setHeight(entity.getProductEntity().getHeight());
        cartProductDTO.setWidth(entity.getProductEntity().getWidth());
        cartProductDTO.setLength(entity.getProductEntity().getLength());
        cartProductDTO.setWeight(entity.getProductEntity().getWeight());
        cartProductDTO.setProductClassifiedBy01(entity.getProductEntity().getClassifiedBy01());
        cartProductDTO.setProductClassifiedBy02(entity.getProductEntity().getClassifiedBy02());
        cartProductDTO.setDiscount(entity.getDiscount());
        cartProductDTO.setAmount(entity.getAmount());
        return cartProductDTO;
    }
}
