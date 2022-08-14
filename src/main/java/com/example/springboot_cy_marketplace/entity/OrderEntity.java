package com.example.springboot_cy_marketplace.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_quantity")
    private Integer totalQuantity;
    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "shipping_fee")
    private Double shippingFee;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_token")
    private String paymentToken;

    @Column(name = "province_name")
    private String provinceName;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "home_address")
    private String homeAddress;

    @Column(name = "status")
    private String status;

    @Column(name = "review_status")
    private boolean reviewStatus = false;

    @Column(name = "discount_product")
    private Double discountProduct = 0.0;

    @Column(name = "discount_free_ship")
    private Double discountFreeShip = 0.0;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "review_id")
    private ReviewEntity reviewEntity;

    @Column(name = "cost_price")
    private Double costPrice = 0.0;

    private Double profit = 0.0;

    @Column(name = "total_discount")
    private Double totalDiscount = 0.0;
    public OrderEntity(UserEntity userEntity, List<OrderItemEntity> items) {
        this.items = items;
        this.setTotalQuantity(this.totalQuantity == null ? 1 : this.totalQuantity + items.size());
        for (OrderItemEntity item : items) {
            item.setOrder(this);
            this.setTotalPrice(this.totalPrice == null ? item.getQuantity() * Double.parseDouble(item.getItem().getNewPrice()) : this.totalPrice + (item.getQuantity() * Double.parseDouble(item.getItem().getNewPrice())));
        }
        this.userEntity = userEntity;
    }
}
