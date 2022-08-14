package com.example.springboot_cy_marketplace.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
@Entity
@Builder
public class ReviewEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "rate")
    private int rate;
    @Column(name = "image")
    private String image;
    @Column(name = "reviewtext")
    private String reviewtext;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="product_id", nullable=false)
    private ProductEntity product;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity userEntity;
    @OneToOne(mappedBy = "reviewEntity", cascade = CascadeType.PERSIST)
    private OrderEntity orderEntity;
}
