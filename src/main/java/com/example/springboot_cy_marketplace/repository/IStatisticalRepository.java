package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.StatisticalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IStatisticalRepository extends JpaRepository<StatisticalEntity, Long> {

    @Query(value = "SELECT s.total_view FROM statistical s WHERE s.day = ?1 AND s.month = ?2 AND s.year = ?3", nativeQuery = true)
    Long findTotalViewByDate(int day, int month, int year);

    @Query(value = "SELECT * FROM statistical s WHERE s.day = ?1 AND s.month = ?2 AND s.year = ?3", nativeQuery = true)
    StatisticalEntity findByDate(int day, int month, int year);
}
