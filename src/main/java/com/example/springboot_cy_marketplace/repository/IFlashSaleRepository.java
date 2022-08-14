package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.dto.FlashSaleDTO;
import com.example.springboot_cy_marketplace.entity.FlashSaleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IFlashSaleRepository extends JpaRepository<FlashSaleEntity, Long> {
    boolean existsByStartDate(LocalDateTime startDate);

    Page<FlashSaleEntity> findByEndDateAfter(LocalDateTime endDate, Pageable pageable);

    @Query(value = "SELECT f FROM FlashSaleEntity f WHERE f.name LIKE %?1% OR f.description LIKE %?1%")
    Page<FlashSaleEntity> findByKeyword(String keyword, Pageable pageable);

    @Query(value = "SELECT f FROM FlashSaleEntity f WHERE f.startDate BETWEEN ?1 AND ?2")
    Page<FlashSaleEntity> findByDate(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query(value = "SELECT * FROM flash_sale WHERE start_date = ?1", nativeQuery = true)
    FlashSaleEntity findByStartDate(String startDate);

    @Query(value = "SELECT * FROM flash_sale WHERE flash_sale.start_date LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<FlashSaleEntity> findAllFlashSaleInDay(String day);

    @Query(value = "SELECT * FROM flash_sale WHERE flash_sale.id = ?1", nativeQuery = true)
    Optional<FlashSaleEntity> findById(Long id);
}
