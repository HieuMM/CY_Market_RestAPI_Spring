package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.BaseEntity;
import com.example.springboot_cy_marketplace.entity.RoomChatEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import com.example.springboot_cy_marketplace.entity.UserRoomChatEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoomChatModel{
    private Long id;
    private String message;
    private Long idUserSend;
    private Long idUserReceive;
    private Long idRoom;
    private String nameUserSend;
    private String nameUserReceive;
    private String avatarUserReceive;
    private boolean status;
    private String image;
    private String video;
    private Date modifiedDate;
    private String container;
    private int quantity;

    public static UserRoomChatModel entityToModel(UserRoomChatEntity object){
        return UserRoomChatModel.builder()
                .id(object.getId())
                .message(object.getMessage())
                .idUserSend(object.getUserEntitySend().getId())
                .idUserReceive(object.getUserEntityReceive().getId())
                .idRoom(object.getRoomChatEntity().getId())
                .nameUserSend(object.getUserEntitySend().getFullName())
                .nameUserReceive(object.getUserEntityReceive().getFullName())
                .status(object.isStatus())
                .image(object.getImage())
                .video(object.getVideo())
                .modifiedDate(object.getCreateDate())
//                .avatarUserReceive(object.getUserEntitySend().getAvatar())
                .container(object.getContainer())
                .quantity(object.getQuantity())
                .build();
    }
}
