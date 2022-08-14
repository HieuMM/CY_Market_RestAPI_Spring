package com.example.springboot_cy_marketplace.repository;

import com.example.springboot_cy_marketplace.entity.UserRoomChatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserRoomChatRepository extends JpaRepository<UserRoomChatEntity,Long> {
    @Query("SELECT us FROM UserRoomChatEntity us \n " +
            "WHERE us.userEntityReceive.id = :id OR us.userEntitySend.id = :id \n" +
            "GROUP BY us.roomChatEntity.id \n ")
    Page<UserRoomChatEntity> findAllRoomChatOfUser(@Param(value = "id") Long idUser, Pageable pageable);
    Page<UserRoomChatEntity> findAllByRoomChatEntityId(Long idRoom, Pageable pageable);
    List<UserRoomChatEntity> findAllByRoomChatEntityId(Long idRoom);
    @Query("SELECT COUNT(us.id) FROM UserRoomChatEntity us WHERE us.roomChatEntity.id = ?1 AND us.status = false AND us.userEntityReceive.id = ?2")
    int countMessageNotSeen(Long idRoom,Long idUserSend);

    @Query(value = "SELECT * FROM user_roomchat WHERE id_roomchat = ?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    UserRoomChatEntity findLastMessageInRoomchat(Long idRoom);
}