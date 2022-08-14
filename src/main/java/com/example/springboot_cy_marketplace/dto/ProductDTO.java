package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private MultipartFile coverImage;
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
    private Boolean status;
    private Long idUserSale;
    private List<ProductClassifiedDTO> productClassifiedDTOList;
    private Long categoryId;
    private boolean enabled;
    private int costPrice;

    public static ProductEntity dtoToEntity(ProductDTO object){
        return ProductEntity.builder()
                .id(object.getId())
                .name(object.getName())
                .description(object.getDescription())
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
                .provinceName(object.getProvinceName())
                .status(object.getStatus())
               // .branchId(object.getBranchId())
             //   .productCategoryEntity(object.getCategoryId())
                .enabled(object.isEnabled())
                .costPrice(object.getCostPrice())
                .build();
    }

}
