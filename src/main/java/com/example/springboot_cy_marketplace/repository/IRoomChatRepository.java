package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.RoomChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoomChatRepository extends JpaRepository<RoomChatEntity,Long> {
}
