package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AddToCartDTO {
    private Long userId; // Mã tài khoản người dùng
    private Long productClassifiedId; // Mã phân loại sản phẩm
    private Integer quantity; // Số lượng sản phẩm
}
