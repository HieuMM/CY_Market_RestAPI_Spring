package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PlaceOrderDTO {
    private Long userId;
    private String paymentMethod;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String homeAddress;
    private Double shippingFee;
    private Double totalDiscount;
    private Double discountProduct;
    private Double discountFreeShip;
}
