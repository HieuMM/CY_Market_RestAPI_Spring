package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.ProductCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
    ProductCategoryEntity findByNameIgnoreCase(String name);

    List<ProductCategoryEntity> findAllByParentCategoryIsNull();

    Page<ProductCategoryEntity> findAllByParentCategoryIsNull(Pageable pageable);

    List<ProductCategoryEntity> findAllByParentCategoryId(Long id);

    Page<ProductCategoryEntity> findAllByParentCategoryId(Long id, Pageable pageable);

    //tong so danh muc cha
    @Query("SELECT count(r.id) FROM ProductCategoryEntity r where r.parentCategory is null ")
    int categoryParent();
    //tong so danh muc con cua cha
    @Query("SELECT count(r.id) FROM ProductCategoryEntity r where r.parentCategory.id=:id")
    int categoryChild(@Param("id") Long id);
}
