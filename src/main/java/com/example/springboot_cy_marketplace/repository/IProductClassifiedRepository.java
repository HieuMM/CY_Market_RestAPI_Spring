package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.ProductClassifiedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProductClassifiedRepository extends JpaRepository<ProductClassifiedEntity,Long> {
    @Query(value = "SELECT pro FROM ProductClassifiedEntity pro WHERE pro.classifyName1 = ?1 and pro.classifyName2 = ?2 and pro.productEntity.id = ?3")
    ProductClassifiedEntity findByClassifiedName(String classified01, String classified02,Long id);

//    ProductClassifiedEntity findByClassifyName1AndClassifyName2aAndProductClassified(String classified01, String classified02,Long id);
}
