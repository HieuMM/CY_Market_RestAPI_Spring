package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.FlashSaleItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IFlashSaleItemRepository extends JpaRepository<FlashSaleItemEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM flash_sale_item WHERE flash_sale_item.id = ?1", nativeQuery = true)
    void deleteByIdCustom(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE product_classified SET product_classified.new_price = ?1 WHERE product_classified.id = ?2", nativeQuery = true)
    void updateNewPrice(String newPrice, Long id);
}
