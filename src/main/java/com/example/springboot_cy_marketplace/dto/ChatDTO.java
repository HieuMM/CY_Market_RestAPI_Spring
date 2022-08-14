package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.UserRoomChatEntity;
import com.example.springboot_cy_marketplace.model.UserRoomChatModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    private Long id;
    private Long idUserSend;
    private Long idUserReceive;
    private Long idRoom;
    private String nameUserSend;
    private String nameUserReceive;
    private String message;
    private boolean status;
    private Date modifiedDate;
    private String image;
    private String video;
    private String container;
    private int quantity;

    public static ChatDTO convert(UserRoomChatModel object){
        return ChatDTO.builder()
                .id(object.getId())
                .idUserSend(object.getIdUserSend())
                .idUserReceive(object.getIdUserReceive())
                .idRoom(object.getIdRoom())
                .nameUserSend(object.getNameUserSend())
                .nameUserReceive(object.getNameUserReceive())
                .message(object.getMessage())
                .status(object.isStatus())
                .modifiedDate(object.getModifiedDate())
                .image(object.getImage())
                .video(object.getVideo())
                .container(object.getContainer())
                .quantity(object.getQuantity())
                .build();
    }

}
