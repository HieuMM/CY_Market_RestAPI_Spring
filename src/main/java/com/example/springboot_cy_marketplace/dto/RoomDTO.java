package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.UserEntity;
import com.example.springboot_cy_marketplace.entity.UserRoomChatEntity;
import com.example.springboot_cy_marketplace.model.UserRoomChatModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Long idRoom;
    private String message;
    private Long idUserSend;
    private Long idUserReceive;
    private String nameUserReceive;
    private String avatarUserReceive;
    private int quantityMessageNotSeen;
    private String container;
    private int quantity;


}
