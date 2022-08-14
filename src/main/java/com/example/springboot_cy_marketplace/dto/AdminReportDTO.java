package com.example.springboot_cy_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminReportDTO {
    private long totalOrderProcessing;
    private long totalOrderCancel;
    private long totalOrderShipping;
    private long totalOrderDone;
    private long totalWaitingForPayment;
    private long totalProducts;
    private long totalProductsWillBeOutOfStock;
    private long totalProductsLocked;
    private long trafficToday;
    private long trafficYesterday;
    private String caculateTraffic;
    private long viewToday;
    private long viewYesterday;
    private String caculateView;
    private long orderToday;
    private long orderYesterday;
    private String caculateOrder;
    private long userToday;
    private long userYesterday;
    private String caculateUser;
    private double averageRating;
    private String averageHealth;
}
