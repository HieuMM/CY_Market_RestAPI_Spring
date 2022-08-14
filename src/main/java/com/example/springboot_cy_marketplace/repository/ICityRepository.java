package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICityRepository extends JpaRepository<CityEntity,Long> {
}
