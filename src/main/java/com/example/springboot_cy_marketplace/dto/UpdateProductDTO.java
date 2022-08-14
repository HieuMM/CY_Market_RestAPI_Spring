package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDTO {
    private Long id;
    private String name;
    private String description;
    private MultipartFile coverImage;
    private String coverImageString;
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
    private List<UpdateProductClassifiedDTO> productClassifiedDTOList;
    private ProductCategoryDTO productCategoryDTO;
    private Long categoryId;
    private boolean enabled;
    private int totalSoled;
    private int costPrice;

    public static UpdateProductDTO entityToDTO(ProductEntity object){
        return UpdateProductDTO.builder()
                .id(object.getId())
                .name(object.getName())
                .description(object.getDescription())
                .coverImageString(object.getCoverImage())
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
                .idUserSale(object.getUserEntity().getId())
                .productClassifiedDTOList(object.getProductClassifiedEntityList() == null ? null : object.getProductClassifiedEntityList().stream().map(pro ->
                        UpdateProductClassifiedDTO.entityToDTO(pro)).collect(Collectors.toList()))
                .productCategoryDTO(object.getProductCategoryEntity() != null ? ProductCategoryDTO.entityToDTO(object.getProductCategoryEntity()) : null)
                .categoryId(object.getProductCategoryEntity() != null ? object.getProductCategoryEntity().getId() : null)
                .enabled(object.isEnabled())
                .totalSoled(object.getTotalSold())
                .costPrice(object.getCostPrice())
                .build();
    }

    public static ProductEntity dtoToEntity(UpdateProductDTO object){
        return ProductEntity.builder()
                .id(object.getId())
                .name(object.getName())
                .description(object.getDescription())
                .coverImage(object.getCoverImageString())
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
//                .productClassifiedDTOList(object.getProductClassifiedDTOList() != null ? object.getProductClassifiedDTOList().stream().map(pro ->
//                        UpdateProductClassifiedDTO.convertProduct(pro)).collect(Collectors.toList()) : null)
                .enabled(object.isEnabled())
                .totalSold(object.getTotalSoled())
                .costPrice(object.getCostPrice())
                .build();
    }

}
