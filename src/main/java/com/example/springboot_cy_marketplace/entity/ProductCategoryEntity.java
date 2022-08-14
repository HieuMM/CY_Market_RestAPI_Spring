package com.example.springboot_cy_marketplace.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "product_category")
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "avatar_url")
    private String avatarUrl;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private ProductCategoryEntity parentCategory;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productCategoryEntity")
    private List<ProductEntity> productEntityList;
   /* @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "categoryId")
    private List<BranchEntity> branchEntityList;*/
}
