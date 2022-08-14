package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.Position;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.entity.UserRoomChatEntity;
import com.example.springboot_cy_marketplace.model.UserRoomChatModel;
import com.example.springboot_cy_marketplace.repository.IUserRoomChatRepository;
import com.example.springboot_cy_marketplace.services.IRoomChatService;
import com.example.springboot_cy_marketplace.services.IUserRoomChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class TestResource {

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:08 CH
     * @description-VN:  Test các hàm có các quyền khác nhau
     * @description-EN:  Test functions with different permissions
     * @param:
     * @return:
     *
     * */

    @GetMapping
    public String testRoleNone() {
        return "test role none";
    }

    @RolesAllowed(Position.BUYER)
    @GetMapping("/buyer")
    public String testRoleBuyer() {
        return "test role buyer";
    }

    @RolesAllowed(Position.SELLER)
    @GetMapping("/seller")
    public String testRoleSeller() {
        return "test role seller";
    }

    @RolesAllowed(Position.ADMIN)
    @GetMapping("/admin")
    public String testRoleAdmin() {
        return "test role admin";
    }

    @Autowired
    IUserRoomChatRepository iUserRoomChatRepository;
    @Autowired
    IUserRoomChatService iUserRoomChatService;
    @Autowired
    IRoomChatService iRoomChatService;
    @GetMapping("testne")
    public Object test(){
//        return ResponseDTO.of(iUserRoomChatRepository.findLastMessageInRoomchat(410L).stream().map(data -> UserRoomChatModel.entityToModel(data)),"test ne");
        Pageable pageable = PageRequest.of(0,20);
        List<UserRoomChatEntity> chatEntityList = new ArrayList<>();
        iUserRoomChatService.findAllRoomChat((long) 98, pageable).stream().forEach(userRoomChatModel ->
                chatEntityList.add(iUserRoomChatService.lastMessage(userRoomChatModel.getIdRoom())));
        return ResponseDTO.of(chatEntityList.stream().map(data ->iRoomChatService.convertRoomChat((long) 98,UserRoomChatModel.entityToModel(data))), "Find all room chat");

//        Pageable pageable = PageRequest.of(0,20);
//        return ResponseDTO.of(iUserRoomChatService.findAllRoomChat((long) 60, pageable).map(userRoomChatEntity -> iRoomChatService.convertRoomChat((long) 60,userRoomChatEntity)), "Find all room chat");
    }
}
