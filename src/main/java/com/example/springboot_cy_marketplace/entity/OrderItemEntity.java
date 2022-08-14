package com.example.springboot_cy_marketplace.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id")
    private ProductClassifiedEntity item;

    private Integer quantity;

    private Double price;

    private String classified1 = "";

    private String classified2 = "";
    public OrderItemEntity(ProductClassifiedEntity product, Integer quantity, Double price, String classified1, String classified2) {
        this.item = product;
        this.quantity = quantity;
        this.price = price;
        this.classified1 = classified1;
        this.classified2 = classified2;
    }
}
