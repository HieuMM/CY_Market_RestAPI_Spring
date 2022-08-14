package com.example.springboot_cy_marketplace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_classified")
public class ProductClassifiedEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "classify_name_1")
    private String classifyName1;
    @Column(name = "classify_name_2")
    private String classifyName2;
    @Column(name = "new_price")
    private String newPrice;
    @Column(name = "old_price")
    private String oldPrice;
    private int discount;
    private int amount;
    private String image;

    @Column(name = "total_sold")
    private int totalSold = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<CartItemEntity> carts = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<OrderItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "productClassified", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<FlashSaleItemEntity> flashSaleItems = new ArrayList<>();
}
