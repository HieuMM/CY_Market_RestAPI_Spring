package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.RoomDTO;
import com.example.springboot_cy_marketplace.entity.RoomChatEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import com.example.springboot_cy_marketplace.model.RoomChatModel;
import com.example.springboot_cy_marketplace.model.UserRoomChatModel;
import com.example.springboot_cy_marketplace.repository.IRoomChatRepository;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import com.example.springboot_cy_marketplace.repository.IUserRoomChatRepository;
import com.example.springboot_cy_marketplace.services.IRoomChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoomChatServiceImpl implements IRoomChatService {
    @Autowired
    IRoomChatRepository iRoomChatRepository;
    @Autowired
    IUserRoomChatRepository iUserRoomChatRepository;
    @Autowired
    IUserRepository iUserRepository;

    @Override
    public List<RoomChatEntity> findAll() {
        return null;
    }

    @Override
    public Page<RoomChatEntity> findAll(Pageable page) {
        return null;
    }

    @Override
    public RoomChatEntity findById(Long id) {
        return null;
    }

    @Override
    public RoomChatEntity add(RoomChatModel dto) {
        return iRoomChatRepository.save(new RoomChatEntity(dto.getId(),null,null));
    }

    @Override
    public List<RoomChatEntity> add(List<RoomChatModel> dto) {
        return null;
    }

    @Override
    public RoomChatEntity update(RoomChatModel dto) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }

    @Override
    public RoomDTO convertRoomChat(Long idUserSend, UserRoomChatModel object) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setIdRoom(object.getIdRoom());
        roomDTO.setQuantityMessageNotSeen(iUserRoomChatRepository.countMessageNotSeen(object.getIdRoom(),idUserSend));
        roomDTO.setMessage(object.getMessage());
        roomDTO.setContainer(object.getContainer());


        if (idUserSend == object.getIdUserSend()){
            roomDTO.setIdUserSend(object.getIdUserSend());
            roomDTO.setIdUserReceive(object.getIdUserReceive());
            roomDTO.setNameUserReceive(object.getNameUserReceive());
//            roomDTO.setAvatarUserReceive(object.getAvatarUserReceive());
            UserEntity userReceive = iUserRepository.findById(object.getIdUserReceive()).get();
            roomDTO.setAvatarUserReceive(userReceive.getAvatar());
        }else{
            roomDTO.setIdUserSend(object.getIdUserReceive());
            roomDTO.setIdUserReceive(object.getIdUserSend());
            roomDTO.setNameUserReceive(object.getNameUserSend());
//            roomDTO.setAvatarUserReceive(object.getAvatarUserReceive());
            UserEntity userReceive = iUserRepository.findById(object.getIdUserSend()).get();
            roomDTO.setAvatarUserReceive(userReceive.getAvatar());
        }
        return roomDTO;
    }
}
