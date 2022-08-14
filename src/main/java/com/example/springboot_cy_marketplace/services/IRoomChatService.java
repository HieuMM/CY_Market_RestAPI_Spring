package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.RoomDTO;
import com.example.springboot_cy_marketplace.entity.AddressEntity;
import com.example.springboot_cy_marketplace.entity.RoomChatEntity;
import com.example.springboot_cy_marketplace.model.AddressModel;
import com.example.springboot_cy_marketplace.model.RoomChatModel;
import com.example.springboot_cy_marketplace.model.UserRoomChatModel;

public interface IRoomChatService extends IBaseService<RoomChatEntity,RoomChatModel, Long> {
    RoomDTO convertRoomChat(Long idUserSend, UserRoomChatModel object);
}
