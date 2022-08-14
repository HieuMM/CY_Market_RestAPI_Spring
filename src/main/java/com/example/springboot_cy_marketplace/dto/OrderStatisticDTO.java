package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderStatisticDTO {
    private long totalOrder;
    private long totalOrderProcessing;
    private long totalOrderCancel;
    private long totalOrderShipping;
    private long totalOrderDone;
    private long totalWaitingForPayment;
    private long totalPayPalPayment;
    private long totalVnPayPayment;
    private long totalCodPayment;
}
