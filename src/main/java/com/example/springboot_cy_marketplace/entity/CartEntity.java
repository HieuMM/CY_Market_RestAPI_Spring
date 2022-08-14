package com.example.springboot_cy_marketplace.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "cart")
public class CartEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(name = "total_price")
    private Double totalPrice;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItemEntity> items;

    public CartEntity(UserEntity userEntity, CartItemEntity... items){
        this.items = Stream.of(items).collect(Collectors.toList());
        this.setTotalQuantity(this.totalQuantity == null ? 1 : this.totalQuantity + items.length);
        for(CartItemEntity item : items){
            item.setCart(this);
            this.setTotalPrice(this.totalPrice == null ? item.getQuantity() * Double.parseDouble(item.getItem().getNewPrice()) : this.totalPrice +
                    (item.getQuantity() * Double.parseDouble(item.getItem().getNewPrice())));
        }
        this.userEntity = userEntity;
    }
}
