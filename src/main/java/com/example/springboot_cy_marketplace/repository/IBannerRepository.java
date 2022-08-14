package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IBannerRepository extends JpaRepository<BannerEntity,Long> {
    @Query("SELECT count(r.id) FROM BannerEntity r")
    int totalBanner();
}
