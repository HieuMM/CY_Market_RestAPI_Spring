package com.example.springboot_cy_marketplace.entity;

import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "max_new_price")
    private int maxNewPrice;
    @Column(name = "min_new_price")
    private int minNewPrice;
    @Column(name = "min_old_price")
    private int minOldPrice;
    @Column(name = "max_old_price")
    private int maxOldPrice;
    private int discount;
    @Column(name = "classified_by_01")
    private String classifiedBy01;
    @Column(name = "classified_by_02")
    private String classifiedBy02;
    @Column(name = "total_amount")
    private int totalAmount;
    @Column(name = "province_id")
    private Long provinceId;
    @Column(name = "province_name")
    private String provinceName;
    private int weight;
    private int length;
    private int width;
    private boolean enabled;
    private int height;
    private Boolean status; //new or second hand
    @Column(name = "total_sold")
    private int totalSold = 0;
    @Column(name = "cost_price")
    private int costPrice = 0;
    @Column(name = "flash_sale")
    private boolean isFlashSale = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_sale")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.PERSIST)
    private List<ProductClassifiedEntity> productClassifiedEntityList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private ProductCategoryEntity productCategoryEntity;

    @OneToMany(mappedBy="product")
    private List<ReviewEntity> reviewList;

//    @OneToMany(mappedBy="product", cascade = CascadeType.ALL)
//    private List<CartProductEntity> cartProductEntities = new ArrayList<>();

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.PERSIST)
    private List<ProductStatisticalEntity> productStatisticalEntityList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductEntity)) return false;
        if (!super.equals(o)) return false;
        ProductEntity that = (ProductEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }
}
