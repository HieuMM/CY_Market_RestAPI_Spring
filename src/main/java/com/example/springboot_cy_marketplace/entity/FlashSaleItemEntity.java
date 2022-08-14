package com.example.springboot_cy_marketplace.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "flash_sale_item", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"flash_sale_id", "product_id"})})
public class FlashSaleItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_price")
    private double currentPrice;

    @Column(name = "current_stock")
    private int currentStock = 0;

    @Column(name = "flash_sale_price")
    private double flashSalePrice;

    @Column(name = "flash_sale_percent")
    private int flashSalePercent;
    @Column(name = "quantity_sale")
    private int quantitySales;

    @Column(name = "limit_per_customer")
    private int limitPerCustomer;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "flash_sale_id")
    private FlashSaleEntity flashSale;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "product_id")
    private ProductClassifiedEntity productClassified;

    public FlashSaleItemEntity(double currentPrice, double flashSalePrice, int flashSalePercent, int quantitySales,
                               int limitPerCustomer, int currentStock, ProductClassifiedEntity productClassified) {
        this.currentPrice = currentPrice;
        this.flashSalePrice = flashSalePrice;
        this.flashSalePercent = flashSalePercent;
        this.quantitySales = quantitySales;
        this.limitPerCustomer = limitPerCustomer;
        this.productClassified = productClassified;
        this.currentStock = currentStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlashSaleItemEntity)) return false;
        FlashSaleItemEntity that = (FlashSaleItemEntity) o;
        return Double.compare(that.getCurrentPrice(), getCurrentPrice()) == 0 && Double.compare(that.getFlashSalePrice(), getFlashSalePrice()) == 0 && getFlashSalePercent() == that.getFlashSalePercent() && getQuantitySales() == that.getQuantitySales() && getLimitPerCustomer() == that.getLimitPerCustomer() && getProductClassified().equals(that.getProductClassified());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCurrentPrice(), getFlashSalePrice(), getFlashSalePercent(), getQuantitySales(), getLimitPerCustomer(), getProductClassified());
    }
}
