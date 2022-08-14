package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Page<ReviewEntity> findAllByProduct_Id(Pageable page, Long id);

    //số lượt rate
    @Query("SELECT count(r.rate) FROM ReviewEntity r where r.product.id=:id")
    int countReview(@Param("id") Long id);

    //tổng số rate
    @Query("SELECT SUM(r.rate) FROM ReviewEntity r where r.product.id=:id")
    int sumRate(@Param("id") Long id);

    @Query(value = "SELECT ROUND(AVG(r.rate), 1) FROM review r", nativeQuery = true)
    Double avgRate();
}
