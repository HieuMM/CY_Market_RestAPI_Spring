package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.config.Position;
import com.example.springboot_cy_marketplace.dto.*;
import com.example.springboot_cy_marketplace.model.NoticesLocalModel;
import com.example.springboot_cy_marketplace.services.AmazonClient;
import com.example.springboot_cy_marketplace.services.INoticesService;
import com.example.springboot_cy_marketplace.services.impl.NoticesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notices")
public class NoticesLocalResource {
    @Autowired
    INoticesService iNoticesService;
    @Autowired
    NoticesServiceImpl noticesService;
    @Autowired
    AmazonClient amazonClient;

    /*
    * @author: HaiPhong
    * @since: 10/06/2022 2:59 CH
    * @description-VN:  Lấy tất cả danh sách thông báo local
    * @description-EN:  Get all local notifications list
    * @param:   pageable
    * @return:
    *
    * */
    @GetMapping
    public Object getAllNoticesLocal(Pageable pageable){
        return ResponseDTO.of(iNoticesService.findAll(pageable).map(notices -> NoticesLocalDTO.modelToDTO(notices)),"Find all notices local");
    }
    /*
    * @author: HaiPhong
    * @since: 10/06/2022 3:01 CH
    * @description-VN:  Lấy thông tin của một thông báo
    * @description-EN:  Get information of a notification
    * @param:   id
    * @return:
    *
    * */
    @GetMapping("/find-by-id/{id}")
    public Object getNoticesLocal(@PathVariable Long id){
        return ResponseDTO.of(NoticesLocalDTO.modelToDTO(iNoticesService.findById(id)),"Find notices local by id user");
    }
    /*
    * @author: HieuMM
    * @since: 20-Jun-22 9:17 AM
    * @description-VN: Lấy thông báo mới nhất của từng người dùng
    * @description-EN:  Get new notification every user
    * @param: id-user
    * */
    @GetMapping("/find-by-userid")
    public Object getNewNoticesLocalEveryUser(@RequestParam(value = "id") Long id){
        List<NoticesLocalDTO> result =noticesService.getNoticesForUserDescTime(id);
        if(result == null){
            return ResponseDTO.show(400, "Find product category by id", null);
        }else {
            return ResponseDTO.show(200, "Find product category by id", result);
        }
    }
    /*
    * @author: HaiPhong
    * @since: 10/06/2022 3:03 CH
    * @description-VN:  Lưu một thông báo local
    * @description-EN:  Save a local notification
    * @param:   request
    * @return:
    *
    * */
    @RolesAllowed(Position.ADMIN)
    @PostMapping
    public Object saveNoticesLocal(AddNotificationDTO request){
        String img1 = "", img2 = "", img3 = "";
        try{
            img1 = amazonClient.uploadFilewithFolder(request.getImg()[0], "notification");
            img2 = amazonClient.uploadFilewithFolder(request.getImg()[1], "notification");
            img3 = amazonClient.uploadFilewithFolder(request.getImg()[2], "notification");
        }catch (Exception e){

        }
        NoticesLocalDTO dto = new NoticesLocalDTO();
        dto.setTitle(request.getTitle());
        dto.setContent(request.getContent());
        dto.setImg1(img1);
        dto.setImg2(img2);
        dto.setImg3(img3);
        dto.setIdUser(request.getIdUser() != null ? request.getIdUser() : null);
        return ResponseDTO.of(NoticesLocalDTO.modelToDTO(iNoticesService.add(dto)),"Save notices local");
    }

    /*
    * @author: HaiPhong
    * @since: 10/06/2022 3:04 CH
    * @description-VN:  Chỉnh sửa nội dung của một thông báo
    * @description-EN:  Edit the content of a message
    * @param:   noticesLocalDTO
    * @return:
    *
    * */
    @RolesAllowed(Position.ADMIN)
    @PutMapping
    public Object updateNoticesLocal(UpdateNoticesLocalDTO noticesLocalDTO){
        return ResponseDTO.of(NoticesLocalDTO.modelToDTO(iNoticesService.updateNoticesLocal(noticesLocalDTO)),"Update notices local");
    }
    
    /*
    * @author: HaiPhong
    * @since: 10/06/2022 3:13 CH
    * @description-VN:  Xóa thông báo
    * @description-EN:  Delete notificationis
    * @param: id
    * @return:
    *
    * */
    @RolesAllowed(Position.ADMIN)
    @DeleteMapping("/{id}")
    public Object deleteNoticesLocal(@PathVariable("id") int id){
        return ResponseDTO.of(iNoticesService.deleteById((long) id),"Delete notices local");
    }

    /*
    * @author: HaiPhong
    * @since: 10/06/2022 3:15 CH
    * @description-VN:  Lấy danh sách câu hỏi của một buyer
    * @description-EN:  Get a list of questions from a buyer
    * @param:   id
    * @return:
    *
    * */
    @RolesAllowed(Position.BUYER)
    @GetMapping("/find-by-id-buyer")
    public Object getNoticesLocalBuyer(@RequestParam(value = "id") Long id){
        return ResponseDTO.of(NoticesLocalDTO.modelToDTO(iNoticesService.findById(id)),"Find notices local by buyer id");
    }
    /*
    * @author: HaiPhong
    * @since: 06/07/2022 11:24 SA
    * @description-VN:  Lấy danh sách thông báo của một người dùng
    * @description-EN:  Get a list of a user's notifications
    * @param: id, pageable
    * @return: 
    *
    * */
//    @RolesAllowed(Position.BUYER)
    @GetMapping("/find-all-owner-notices")
    public Object getNoticesLocalAndOwner(@RequestParam(value = "id") Long id, Pageable pageable){
        return ResponseDTO.of(iNoticesService.findAllOwnerNotices(id, pageable),"Find all owner notices by buyer id");
    }

}
