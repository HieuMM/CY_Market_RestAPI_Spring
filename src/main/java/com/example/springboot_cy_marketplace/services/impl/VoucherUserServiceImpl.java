package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.VoucherUserDTO;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import com.example.springboot_cy_marketplace.entity.VoucherEntity;
import com.example.springboot_cy_marketplace.entity.VoucherUserEntity;
import com.example.springboot_cy_marketplace.model.VoucherUserModel;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import com.example.springboot_cy_marketplace.repository.IVoucherRepository;
import com.example.springboot_cy_marketplace.repository.IVoucherUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class VoucherUserServiceImpl {
    @Autowired
    IVoucherUserRepository voucherUserRepository;
    @Autowired
    IVoucherRepository voucherRepository;
    @Autowired
    IUserRepository userRepository;

    /*
     * @author: HieuMM
     * @since: 05-Jul-22 2:05 PM
     * @description-VN:  Người dùng lấy voucher
     * @description-EN:
     * @param:
     * */
    public Boolean getVoucher(VoucherUserDTO voucherUserDTO) {
        VoucherUserEntity voucherUserEntity = new VoucherUserEntity();
        VoucherEntity voucherEntity = voucherRepository.findById(voucherUserDTO.getVoucherId()).get();
        UserEntity userEntity = userRepository.findById(voucherUserDTO.getUserId()).get();
        int countUserVoucherSaved = voucherUserRepository.countByVoucherEntityAndAndUserEntity(userEntity.getId(), voucherEntity.getId());
        if (countUserVoucherSaved == 0) {
            voucherUserEntity.setVoucherEntity(voucherEntity);
            voucherUserEntity.setUserEntity(userEntity);
            voucherUserEntity.setStatus(false);
            voucherUserRepository.save(voucherUserEntity);
            return true;
        } else {
            return false;
        }

    }

    /*
     * @author: HieuMM
     * @since: 11-Jul-22 8:38 AM
     * @description-VN:  Kiểm tra điều kiện sử dụng voucher
     * @description-EN:
     * @param:
     * */
    public String checkVoucher(Long idVoucher,int money) {
        VoucherUserEntity voucherUserEntity = voucherUserRepository.findById(idVoucher).get();
        VoucherEntity voucherEntity = voucherRepository.findById(voucherUserEntity.getVoucherEntity().getId()).get();
        Date date = new Date();
        int checkOver = date.compareTo(voucherEntity.getEndDate());
        int checkIn = date.compareTo(voucherEntity.getStartDate());
        if (checkOver >0) {
            return "Voucher hết hạn sử dụng !";
        } else if (checkIn <0) {
            return "Voucher chưa đến thời gian sử dụng !";
        } else if(voucherEntity.getAmountUsed() >= voucherEntity.getAmountVoucher()) {
            return "Voucher đã hết lượt sử dụng !";
        }else if (voucherEntity.getMaxValueCanReduce() < money) {
            return "Giá trị đơn hàng không phù hợp áp dụng Voucher !";
        }else if(voucherEntity.getMinValueOrder() > money) {
            return "Giá trị đơn hàng không phù hợp áp dụng Voucher !";
        } else {
            return "OK";
        }
    }

    /*
     * @author: HieuMM
     * @since: 05-Jul-22 2:18 PM
     * @description-VN:  Người dùng sử dụng voucher
     * @description-EN:
     * @param:
     * */


    public Boolean usedVoucher(Long id) {
        VoucherUserEntity voucherUserEntity = voucherUserRepository.findById(id).get();
        VoucherEntity voucherEntity = voucherRepository.findById(voucherUserEntity.getVoucherEntity().getId()).get();
       /* Date date = new Date();
        int checkOver = date.compareTo(voucherEntity.getEndDate());
        int checkIn = date.compareTo(voucherEntity.getStartDate());
        if (voucherEntity.getAmountUsed() >= voucherEntity.getAmountVoucher() || checkOver > 0 || checkIn < 0) {
            return false;
        } else {*/
            int used = voucherEntity.getAmountUsed();
            voucherEntity.setAmountUsed(used + 1);
            voucherRepository.save(voucherEntity);
            voucherUserEntity.setStatus(true);
            voucherUserRepository.save(voucherUserEntity);
            return true;
        /*}*/
    }

    /*
     * @author: HieuMM
     * @since: 05-Jul-22 2:36 PM
     * @description-VN:  Lấy danh sách voucher của từng user
     * @description-EN:
     * @param:
     * */
    public Page<VoucherUserModel> findAllVoucherByIdUser(Pageable pageable, Long id) {
        return voucherUserRepository.findAllByUserEntity_Id(pageable, id).map(VoucherUserModel::entityToDto);
    }

}
