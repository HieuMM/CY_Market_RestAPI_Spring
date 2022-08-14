package com.example.springboot_cy_marketplace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_roomchat")
public class UserRoomChatEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private boolean status;
    private String image;
    private String video;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id_send")
    private UserEntity userEntitySend;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id_receive")
    private UserEntity userEntityReceive;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_roomchat")
    private RoomChatEntity roomChatEntity;

    private String container;
    private int quantity;

}
