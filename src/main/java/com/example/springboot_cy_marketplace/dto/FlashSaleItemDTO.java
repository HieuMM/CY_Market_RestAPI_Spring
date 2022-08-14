package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.model.ProductCategoryModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FlashSaleItemDTO {
    private Long id;
    private int flashSalePercent;
    private double flashSalePrice;
    private int quantitySales;
    private int limitPerCustomer;
    private Long productId;
    private String productName;
    private String productCoverImage;
    private int minNewPrice;
    private int maxNewPrice;
    private int minOldPrice;
    private int maxOldPrice;
    private int totalAmount;
    private Long categoryId;
    private ProductCategoryModel productCategoryModel;
    private Boolean status;
    private boolean enabled;

    public FlashSaleItemDTO(Long id, int flashSalePercent, double flashSalePrice, int quantitySales,
                            int limitPerCustomer) {
        this.id = id;
        this.flashSalePercent = flashSalePercent;
        this.flashSalePrice = flashSalePrice;
        this.quantitySales = quantitySales;
        this.limitPerCustomer = limitPerCustomer;
    }
}
