package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.dto.ChatDTO;
import com.example.springboot_cy_marketplace.dto.OffsetBasedPageRequestDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.dto.RoomDTO;
import com.example.springboot_cy_marketplace.entity.UserRoomChatEntity;
import com.example.springboot_cy_marketplace.model.RoomChatModel;
import com.example.springboot_cy_marketplace.model.UserRoomChatModel;
import com.example.springboot_cy_marketplace.repository.IUserRoomChatRepository;
import com.example.springboot_cy_marketplace.services.AmazonClient;
import com.example.springboot_cy_marketplace.services.IRoomChatService;
import com.example.springboot_cy_marketplace.services.IUserRoomChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SocketResource {
    @Autowired
    IRoomChatService iRoomChatService;
    @Autowired
    IUserRoomChatService iUserRoomChatService;
    @Autowired
    AmazonClient amazonClient;

    @MessageMapping("/chat/{idUserReceive}/{idRoom}")
    @SendTo("/sendto/message/{idUserReceive}")
    public Object chat(ChatDTO chatDTO, @DestinationVariable Long idUserReceive, @DestinationVariable Long idRoom){
        if (chatDTO.getIdRoom() == null){
            chatDTO.setIdRoom(iRoomChatService.add(new RoomChatModel(chatDTO.getIdRoom(), null)).getId());
        }
        return ResponseDTO.of(ChatDTO.convert(iUserRoomChatService.saveNewMessage(chatDTO)),"Chat");
    }

    @MessageMapping("/chat/{idUserSend}")
    @SendTo("/sendto/new-message/{idUserSend}")
    public Object newMessage(ChatDTO chatDTO, @DestinationVariable Long idUserSend){
        if (chatDTO.getIdRoom() == null){
            chatDTO.setIdRoom(iRoomChatService.add(new RoomChatModel(chatDTO.getIdRoom(), null)).getId());
        }

        return ResponseDTO.of(ChatDTO.convert(iUserRoomChatService.saveNewMessage(chatDTO)),"Chat new message");
    }

    @MessageMapping("/admin-chat/{idUserReceive}")
    @SendTo("/sendto/new-message/{idUserReceive}")
    public Object newAdminMessage(ChatDTO chatDTO, @DestinationVariable Long idUserReceive){
        if (chatDTO.getIdRoom() == null){
            chatDTO.setIdRoom(iRoomChatService.add(new RoomChatModel(chatDTO.getIdRoom(), null)).getId());
        }

        return ResponseDTO.of(ChatDTO.convert(iUserRoomChatService.saveNewMessage(chatDTO)),"Chat new message");
    }


    @MessageMapping("/getAllRoom/{idUserSend}")
    @SendTo("/sendto/room/{idUserSend}")
    public Object getAllRoom(int id){
//        Pageable pageable = PageRequest.of(0,20);
//        return ResponseDTO.of(iUserRoomChatService.findAllRoomChat((long) id, pageable).map(userRoomChatEntity -> iRoomChatService.convertRoomChat((long) id,userRoomChatEntity)), "Find all room chat");

        Pageable pageable = PageRequest.of(0,20);
        List<UserRoomChatEntity> chatEntityList = new ArrayList<>();
        iUserRoomChatService.findAllRoomChat((long) id, pageable).stream().forEach(userRoomChatModel ->
                chatEntityList.add(iUserRoomChatService.lastMessage(userRoomChatModel.getIdRoom())));
        return ResponseDTO.of(chatEntityList.stream().map(data ->iRoomChatService.convertRoomChat((long) id,UserRoomChatModel.entityToModel(data))), "Find all room chat");
    }

    @MessageMapping("/getHistoryChat/{idRoom}/{quantity}")
    @SendTo("/sendto/history/{idRoom}")
    public Object getHistoryChat(int id,@DestinationVariable Long idRoom, @DestinationVariable int quantity){

        int count = iUserRoomChatService.findAllByRoomChatEntityId((long) id);
        if (count<=quantity){
            Pageable pageable = new OffsetBasedPageRequestDTO(quantity,0);
            return ResponseDTO.of(iUserRoomChatService.findAllByRoomChatEntityId((long) id, pageable).map(userRoomChatEntity -> ChatDTO.convert(userRoomChatEntity)),"Find all history chat");
        }else {
            Pageable pageable = new OffsetBasedPageRequestDTO(quantity,count-quantity);
            return ResponseDTO.of(iUserRoomChatService.findAllByRoomChatEntityId((long) id, pageable).map(userRoomChatEntity -> ChatDTO.convert(userRoomChatEntity)),"Find all history chat");
        }

    }

    @MessageMapping("/sendding/{idUserReceive}/{idRoom}")
    @SendTo("/sendto/sendding/{idUserReceive}")
    public Object sendding(ChatDTO chatDTO){
        return ResponseDTO.of(new ChatDTO(null,chatDTO.getIdUserSend(), chatDTO.getIdUserReceive(), chatDTO.getIdRoom(),null,null, chatDTO.getMessage(), false,null,null,null,null,0),"Đang soạn tin ... ");
    }

    @Autowired
    IUserRoomChatRepository iUserRoomChatRepository;

    @MessageMapping("/updateStatus/{idUserReceive}/{idRoom}")
    @SendTo("/sendto/update-status/{idUserReceive}")
    public Object updateStatus(int[] ids){
        for (int id: ids) {
            UserRoomChatEntity userRoomChatEntity = iUserRoomChatRepository.findById((long) id).get();
            userRoomChatEntity.setStatus(true);
            iUserRoomChatRepository.save(userRoomChatEntity);
        }
        int lastIndex = 0;
        int size = ids.length;
        if (size > 0){
            lastIndex = ids[size-1];
            return ResponseDTO.of(ChatDTO.convert(UserRoomChatModel.entityToModel(iUserRoomChatRepository.findById((long) lastIndex).get())),"Update status message");
        }else {
            return ResponseDTO.of(new ChatDTO(),"Update status message");
        }
    }

//    @MessageMapping("/updateStatus/{idUserReceive}/{idRoom}")
//    @SendTo("/sendto/update-status/{idUserReceive}")
//    public Object updateStatusLastMessage(int id){
//        UserRoomChatEntity userRoomChatEntity = iUserRoomChatRepository.findById((long) id).get();
//        userRoomChatEntity.setStatus(true);
//        return ResponseDTO.of(ChatDTO.convert(iUserRoomChatRepository.save(userRoomChatEntity)),"Update status last message");
//    }

    @PostMapping("/api/v1/message/upload-video")
    public Object uploadFile(MultipartFile file){
        String url = amazonClient.uploadFilewithFolder(file,"message");
        return ResponseDTO.of(url,"Upload video to S3");
    }

}
