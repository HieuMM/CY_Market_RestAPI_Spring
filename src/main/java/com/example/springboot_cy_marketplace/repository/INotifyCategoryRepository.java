package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.NotifyCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INotifyCategoryRepository extends JpaRepository<NotifyCategoryEntity,Long> {
    NotifyCategoryEntity findByName(String name);
}
