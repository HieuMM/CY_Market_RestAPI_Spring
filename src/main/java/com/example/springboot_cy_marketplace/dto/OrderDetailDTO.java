package com.example.springboot_cy_marketplace.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDetailDTO {
    private Long orderId;
    private Long userId;
    private String status;
    private String createdAt;
    private double totalPrice; // Tổng tiền hàng
    private Double shippingFee;
    private Double totalDiscount; // Tổng tiền giảm giá (mã giảm giá, miễn phí vận chuyển)
    private String paymentMethod; // Phương thức, mặc định là paypal
    private String currency; // Tiền tệ, mặc định là USD
    private String intent; // Mục đích thanh toán
    private Double totalPayment; // Tổng tiền thanh toán
    private Double discountProduct; // Giảm giá sản phẩm
    private Double discountFreeShip; // Giảm giá miễn phí vận chuyển
    private String receiverName;
    private String receiverEmail;
    private String receiverPhone;
    private String receiverAddress;
    private List<CartProductDTO> productList;
    private Integer totalQuantity;
    private String description;
    private boolean reviewStatus;
}
