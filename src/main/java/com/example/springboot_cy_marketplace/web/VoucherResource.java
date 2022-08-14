package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.dto.VoucherDTO;
import com.example.springboot_cy_marketplace.entity.VoucherEntity;
import com.example.springboot_cy_marketplace.model.VoucherModel;
import com.example.springboot_cy_marketplace.services.impl.VoucherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/voucher")
public class VoucherResource {
    @Autowired
    VoucherServiceImpl voucherService;

    /*
    * @author: HieuMM
    * @since: 05-Jul-22 1:38 PM
    * @description-VN:  Lấy tất cả voucher
    * @description-EN:
    * @param:
    * */
    @GetMapping(value = "/getAllVoucher")
    public Object getVoucherById(Pageable pageable) {
        Page<VoucherModel> voucher = voucherService.findAllVoucher(pageable);
        if (voucher == null) {
            return ResponseDTO.of(null, "Get voucher ");
        } else {
            return ResponseDTO.of(voucher, "Get voucher ");
        }
    }

    /*
    * @author: HieuMM
    * @since: 05-Jul-22 9:52 AM
    * @description-VN:  Tạo mới voucher
    * @description-EN:
    * @param:
    * */
    @PostMapping(value = "/add")
    public Object createVoucher(@RequestBody VoucherDTO voucherDTO) {
        VoucherEntity voucherEntity = voucherService.createVoucher(voucherDTO);
        if (voucherEntity == null) {
            return ResponseDTO.of(null, "Create voucher");
        } else {
            return ResponseDTO.of(VoucherDTO.entityToDto(voucherEntity), "Create voucher");
        }
    }
    /*
    * @author: HieuMM
    * @since: 05-Jul-22 10:40 AM
    * @description-VN:  Miêu tả chi tiết voucher theo id
    * @description-EN:
    * @param:
    * */
    @GetMapping(value = "/getById/{id}")
    public Object getVoucherById(@PathVariable(value = "id") Long id) {
        VoucherModel voucher = voucherService.getVoucherById(id);
        if (voucher == null) {
            return ResponseDTO.of(null, "Get voucher by id");
        } else {
            return ResponseDTO.of(VoucherModel.modelToEntity(voucher), "Get voucher by id");
        }
    }
    /*
    * @author: HieuMM
    * @since: 05-Jul-22 10:47 AM
    * @description-VN:  Update voucher theo id
    * @description-EN:
    * @param:
    * */
    @PutMapping(value = "/update")
    public Object updateVoucher(@RequestBody VoucherDTO voucherDTO) {
        VoucherEntity voucherEntity = voucherService.updateVoucher(voucherDTO);
        if (voucherEntity == null) {
            return ResponseDTO.of(null, "Update voucher");
        } else {
            return ResponseDTO.of(VoucherDTO.entityToDto(voucherEntity), "Update voucher");
        }
    }
    /*
    * @author: HieuMM
    * @since: 05-Jul-22 10:40 AM
    * @description-VN:  Xóa voucher theo id
    * @description-EN:
    * @param:
    * */
    @DeleteMapping(value = "/delete/{id}")
    public Object deleteVoucherById(@PathVariable(value = "id") Long id) {
        Boolean status = voucherService.deleteVoucherById(id);
        return ResponseDTO.of(status, "Delete voucher by id");
    }
    /*
    * @author: HieuMM
    * @since: 05-Jul-22 11:20 AM
    * @description-VN:  check code exist or not
    * @description-EN:
    * @param:
    * */
    @GetMapping(value = "/checkCode")
    public Object checkCode( String code) {
        Boolean status = voucherService.checkCodeVoucher(code);
        return ResponseDTO.of(status, "Check code");
    }
}
