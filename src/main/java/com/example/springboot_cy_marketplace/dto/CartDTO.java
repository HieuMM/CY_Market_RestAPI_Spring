package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartDTO implements Serializable {
    private Long id;
    private List<CartProductDTO> productList;
    private Double totalPrice;
    private Integer totalQuantity;
}
