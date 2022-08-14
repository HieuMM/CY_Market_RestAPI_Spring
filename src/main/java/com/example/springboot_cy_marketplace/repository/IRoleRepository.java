package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<RoleEntity,Long> {
    RoleEntity findByName(String name);
}
