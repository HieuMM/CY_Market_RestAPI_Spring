package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.model.ProductModel;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class AdminManageProductDTO {
    private Page<ProductModel> productModelPage;;
    private long totalProduct;
    private long totalProductBlocked;
    private long totalProductWillOutOfStock;
}
