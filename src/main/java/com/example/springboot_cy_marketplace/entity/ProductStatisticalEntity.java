package com.example.springboot_cy_marketplace.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product_statistical")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductStatisticalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int day;
    private int month;
    private int year;
    @Column(name = "total_view")
    private int totalView;
    @Column(name = "total_buy")
    private int totalBuy;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;
}
