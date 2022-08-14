package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByUserEntity_Id(Long userId);

}
