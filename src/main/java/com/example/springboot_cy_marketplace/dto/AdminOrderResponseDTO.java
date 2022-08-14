package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminOrderResponseDTO {
    private Long orderId;
    private String createDate;
    private String paymentMethod;
    private String status;
    private Double costPrice;
    private Double revenue; // Doanh thu
    private Double profit; // Lợi nhuận
}
