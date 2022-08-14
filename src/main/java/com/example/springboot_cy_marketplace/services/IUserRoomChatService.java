package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.ChatDTO;
import com.example.springboot_cy_marketplace.entity.UserRoomChatEntity;
import com.example.springboot_cy_marketplace.model.UserRoomChatModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserRoomChatService extends IBaseService<UserRoomChatEntity, UserRoomChatModel,Long>{
    Page<UserRoomChatModel> findAllRoomChat(Long id, Pageable pageable);

    Page<UserRoomChatEntity> findHistoryRoomChat(Long id_room, Pageable pageable);
    int findAllByRoomChatEntityId(Long id);

    Page<UserRoomChatModel> findAllByRoomChatEntityId(Long id,Pageable pageable);

    UserRoomChatModel saveNewMessage(ChatDTO dto);

    UserRoomChatEntity lastMessage(Long idRoom);
}
