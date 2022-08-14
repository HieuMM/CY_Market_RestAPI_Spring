package com.example.springboot_cy_marketplace.model;


import com.example.springboot_cy_marketplace.dto.ProductCategoryDTO;
import com.example.springboot_cy_marketplace.entity.BranchEntity;
import com.example.springboot_cy_marketplace.entity.ProductEntity;
import com.example.springboot_cy_marketplace.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    private Long id;
    private String name;
    private String description;
    private String coverImage;
    private int maxNewPrice;
    private int minNewPrice;
    private int minOldPrice;
    private int maxOldPrice;
    private int discount;
    private String classifiedBy01;
    private String classifiedBy02;
    private int totalAmount;
    private Long provinceId;
    private String provinceName;
    private int weight;
    private int length;
    private int width;
    private int height;
    private Long idUserSale;
    private Boolean status;
    private List<ProductClassifiedModel> productClassifiedModelList;
    private BranchEntity branchId;
    private Long categoryId;
    private boolean enabled;
    private long totalSold;
    private double averageRating;
    private int costPrice;
    private ProductCategoryModel productCategoryModel;
    private long totalViews;
    private boolean flashSale;

    public static ProductModel entityToModel(ProductEntity object){
        return ProductModel.builder()
                .id(object.getId())
                .name(object.getName())
                .description(object.getDescription())
                .coverImage(object.getCoverImage())
                .maxNewPrice(object.getMaxNewPrice())
                .minNewPrice(object.getMinNewPrice())
                .maxOldPrice(object.getMaxOldPrice())
                .minOldPrice(object.getMinOldPrice())
                .discount(object.getDiscount())
                .classifiedBy01(object.getClassifiedBy01())
                .classifiedBy02(object.getClassifiedBy02())
                .totalAmount(object.getTotalAmount())
                .provinceId(object.getProvinceId())
                .weight(object.getWeight())
                .length(object.getLength())
                .width(object.getWidth())
                .height(object.getHeight())
                .status(object.getStatus())
                .idUserSale(object.getUserEntity().getId())
                .productClassifiedModelList(object.getProductClassifiedEntityList() == null ? null : object.getProductClassifiedEntityList().stream().map(ProductClassifiedModel::entityToModel).collect(Collectors.toList()))
                .provinceName(object.getProvinceName())
                .enabled(object.isEnabled())
                .totalSold(object.getTotalSold())
                .averageRating(object.getReviewList() != null ? object.getReviewList().stream().mapToDouble(ReviewEntity::getRate)
                        .average().orElse(0) : 0)
                .productCategoryModel(object.getProductCategoryEntity() != null ? ProductCategoryModel.entityToModel(object.getProductCategoryEntity()) : null)
                .categoryId(object.getProductCategoryEntity().getId())
                .costPrice(object.getCostPrice())
                .flashSale(object.isFlashSale())
                .build();
    }
}
