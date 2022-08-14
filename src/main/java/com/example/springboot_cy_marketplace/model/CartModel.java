package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartModel {
    private Long id;
    private List<ProductDTO> productDTOList;
    private Double totalPrice;
    private Integer totalQuantity;
}
