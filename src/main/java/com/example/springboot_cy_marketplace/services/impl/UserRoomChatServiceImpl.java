package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.ChatDTO;
import com.example.springboot_cy_marketplace.entity.RoomChatEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import com.example.springboot_cy_marketplace.entity.UserRoomChatEntity;
import com.example.springboot_cy_marketplace.model.UserRoomChatModel;
import com.example.springboot_cy_marketplace.repository.IRoomChatRepository;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import com.example.springboot_cy_marketplace.repository.IUserRoomChatRepository;
import com.example.springboot_cy_marketplace.services.IUserRoomChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoomChatServiceImpl implements IUserRoomChatService {
    @Autowired
    IUserRoomChatRepository iUserRoomChatRepository;
    @Autowired
    IUserRepository iUserRepository;
    @Autowired
    IRoomChatRepository iRoomChatRepository;

    @Override
    public List<UserRoomChatEntity> findAll() {
        return null;
    }

    @Override
    public Page<UserRoomChatEntity> findAll(Pageable page) {
        return null;
    }

    @Override
    public UserRoomChatEntity findById(Long id) {
        return iUserRoomChatRepository.findById(id).get();
    }

    @Override
    public UserRoomChatEntity add(UserRoomChatModel dto) {
        UserEntity userSend = iUserRepository.findById(dto.getIdUserSend()).get();
        UserEntity userReceive = iUserRepository.findById(dto.getIdUserReceive()).get();
        RoomChatEntity roomChatEntity = iRoomChatRepository.findById(dto.getIdRoom()).get();
        return iUserRoomChatRepository.save(new UserRoomChatEntity(null,dto.getMessage(),dto.isStatus(),dto.getImage(),dto.getVideo(),userSend,userReceive,roomChatEntity, dto.getContainer(), dto.getQuantity()));
    }

    @Override
    public List<UserRoomChatEntity> add(List<UserRoomChatModel> dto) {
        return null;
    }

    @Override
    public UserRoomChatEntity update(UserRoomChatModel dto) {
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
    public Page<UserRoomChatModel> findAllRoomChat(Long id, Pageable pageable) {
        return iUserRoomChatRepository.findAllRoomChatOfUser(id,pageable).map(user -> UserRoomChatModel.entityToModel(user));
    }

    @Override
    public Page<UserRoomChatEntity> findHistoryRoomChat(Long id_room, Pageable pageable) {
        return iUserRoomChatRepository.findAllByRoomChatEntityId(id_room,pageable);
    }

    @Override
    public int findAllByRoomChatEntityId(Long id) {
        return iUserRoomChatRepository.findAllByRoomChatEntityId(id).size();
    }

    @Override
    public Page<UserRoomChatModel> findAllByRoomChatEntityId(Long id, Pageable pageable) {
        return iUserRoomChatRepository.findAllByRoomChatEntityId(id, pageable).map(room -> UserRoomChatModel.entityToModel(room));
    }

    @Override
    public UserRoomChatModel saveNewMessage(ChatDTO dto) {
        UserEntity userSend = iUserRepository.findById(dto.getIdUserSend()).get();
        UserEntity userReceive = iUserRepository.findById(dto.getIdUserReceive()).get();
        RoomChatEntity roomChatEntity = iRoomChatRepository.findById(dto.getIdRoom()).get();
        return UserRoomChatModel.entityToModel(iUserRoomChatRepository.save(new UserRoomChatEntity(null,dto.getMessage(),dto.isStatus(),dto.getImage(),dto.getVideo(),userSend,userReceive,roomChatEntity, dto.getContainer(), dto.getQuantity())));

    }

    @Override
    public UserRoomChatEntity lastMessage(Long idRoom) {
        return iUserRoomChatRepository.findLastMessageInRoomchat(idRoom);
    }
}
