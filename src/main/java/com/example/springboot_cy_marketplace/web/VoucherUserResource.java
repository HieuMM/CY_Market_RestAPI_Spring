package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.dto.VoucherDTO;
import com.example.springboot_cy_marketplace.dto.VoucherUserDTO;
import com.example.springboot_cy_marketplace.entity.VoucherUserEntity;
import com.example.springboot_cy_marketplace.services.impl.VoucherUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping(value = "/api/v1/voucheruser")
public class VoucherUserResource {
    @Autowired
    VoucherUserServiceImpl voucherUserService;
    /*
    * @author: HieuMM
    * @since: 05-Jul-22 2:25 PM
    * @description-VN: người dùng nhận voucher
    * @description-EN:
    * @param:
    * */
   @PostMapping(value = "/getVoucher")
    public Object addVoucher(@RequestBody VoucherUserDTO voucherUserDTO){
      // VoucherUserEntity voucherUserEntity=voucherUserService.addVoucherUser(voucherUserDTO);
       Boolean check=voucherUserService.getVoucher(voucherUserDTO);
     /*  if (voucherUserEntity == null) {
           return ResponseDTO.of(null, "Get voucher");
       } else {*/
           return ResponseDTO.of(check, "Get voucher");
     //  }
   }
   /*
   * @author: HieuMM
   * @since: 12-Jul-22 9:37 AM
   * @description-VN:  Check voucher hợp lệ
   * @description-EN:
   * @param:
   * */
    @PostMapping(value = "/checkVoucher/{id}/{money}")
    public Object checkVoucher(@PathVariable Long id ,@PathVariable int money){
        String check=voucherUserService.checkVoucher(id, money);
        if (check.equals("OK")){
            return ResponseDTO.show(200,"Check voucher","OK");
        }else {
            return ResponseDTO.show(400,"Check voucher",check);
        }
    }
   /*
   * @author: HieuMM
   * @since: 05-Jul-22 3:26 PM
   * @description-VN:  update trạng thái voucher sau khi sử dụng
   * @description-EN:
   * @param:
   * */
    @PutMapping(value = "/updateVoucher/{id}")
    public Object updateVoucher(@PathVariable(value = "id") Long id) throws ParseException {
        Boolean voucherUserEntity=voucherUserService.usedVoucher(id);
            return ResponseDTO.of(voucherUserEntity, "Update voucher");
    }
   /*
   * @author: HieuMM
   * @since: 05-Jul-22 3:07 PM
   * @description-VN:  Hiển thị tất cả voucher của người dùng
   * @description-EN:
   * @param:
   * */
    @GetMapping(value = "/getAllVoucher/{id}")
    public Object getVoucherById(Pageable pa, @PathVariable(value = "id") Long id){
            return ResponseDTO.of(voucherUserService.findAllVoucherByIdUser(pa,id), "Get voucher");
    }
}
