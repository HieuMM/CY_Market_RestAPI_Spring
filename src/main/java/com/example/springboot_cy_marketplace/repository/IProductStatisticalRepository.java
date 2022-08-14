package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.ProductEntity;
import com.example.springboot_cy_marketplace.entity.ProductStatisticalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IProductStatisticalRepository extends JpaRepository<ProductStatisticalEntity, Long> {
    ProductStatisticalEntity findByProductEntity(ProductEntity productEntity);
    Optional<ProductStatisticalEntity> findByProductEntity_IdAndDayAndMonthAndYear(Long productId, int day, int month, int year);

    @Query(value = "SELECT SUM(s.total_view) FROM product_statistical s WHERE s.day = ?1 AND s.month = ?2 AND s.year = ?3", nativeQuery = true)
    Long getTotalViewsByDay(int day, int month, int year);
}
