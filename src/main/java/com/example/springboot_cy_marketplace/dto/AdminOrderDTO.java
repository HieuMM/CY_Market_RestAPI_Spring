package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminOrderDTO {
    List<AdminOrderResponseDTO> orderList;
    // Tổng doanh thu tất cả đơn hàng.
    private Double totalRevenue;
    // Tổng giá vốn tất cả đơn hàng.
    private Double totalCostPrice;
    // Tổng lợi nhuận tất cả đơn hàng.
    private Double totalProfit;
}
