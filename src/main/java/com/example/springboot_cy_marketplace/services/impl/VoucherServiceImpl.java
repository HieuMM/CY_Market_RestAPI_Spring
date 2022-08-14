package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.dto.VoucherDTO;
import com.example.springboot_cy_marketplace.entity.VoucherEntity;
import com.example.springboot_cy_marketplace.entity.VoucherUserEntity;
import com.example.springboot_cy_marketplace.model.VoucherModel;
import com.example.springboot_cy_marketplace.repository.IVoucherRepository;
import com.example.springboot_cy_marketplace.repository.IVoucherUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherServiceImpl {
    @Autowired
    IVoucherRepository voucherRepository;

    @Autowired
    IVoucherUserRepository voucherUserRepository;


    /*
     * @author: HieuMM
     * @since: 05-Jul-22 9:41 AM
     * @description-VN:  Tạo voucher mới
     * @description-EN:  Create new voucher
     * @param:
     * */

    public VoucherEntity createVoucher(VoucherDTO voucherDTO) {
        VoucherEntity voucherEntity = new VoucherEntity();
        voucherEntity.setCategoryVoucher(voucherDTO.getCategoryVoucher());
        voucherEntity.setNameProgram(voucherDTO.getNameProgram());
        voucherEntity.setCodeVoucher(voucherDTO.getCodeVoucher());
        voucherEntity.setStartDate(voucherDTO.getStartDate());
        voucherEntity.setEndDate(voucherDTO.getEndDate());
        voucherEntity.setDiscountPercent(voucherDTO.getDiscountPercent());
        voucherEntity.setMinValueOrder(voucherDTO.getMinValueOrder());
        voucherEntity.setMaxValueCanReduce(voucherDTO.getMaxValueCanReduce());
        voucherEntity.setAmountVoucher(voucherDTO.getAmountVoucher());
        voucherEntity.setMaxValueCanReduce(voucherDTO.getMaxValueCanReduce());
        voucherEntity.setAmountUsed(0);
        voucherRepository.save(voucherEntity);
        return voucherEntity;
    }

    /*
     * @author: HieuMM
     * @since: 05-Jul-22 9:43 AM
     * @description-VN:  Show thông tin voucher theo id
     * @description-EN:  Detail voucher by id
     * @param:
     * */
    public VoucherModel getVoucherById(Long id) {
        VoucherEntity voucherEntity = voucherRepository.findById(id).get();
        return VoucherModel.builder()
                .id(voucherEntity.getId())
                .categoryVoucher(voucherEntity.getCategoryVoucher())
                .nameProgram(voucherEntity.getNameProgram())
                .codeVoucher(voucherEntity.getCodeVoucher())
                .startDate(voucherEntity.getStartDate())
                .endDate(voucherEntity.getEndDate())
                .discountPercent(voucherEntity.getDiscountPercent())
                .minValueOrder(voucherEntity.getMinValueOrder())
                .amountVoucher(voucherEntity.getAmountVoucher())
                .maxValueCanReduce(voucherEntity.getMaxValueCanReduce())
                .amountUsed(voucherEntity.getAmountUsed())
                .build();
    }

    /*
     * @author: HieuMM
     * @since: 05-Jul-22 10:43 AM
     * @description-VN:  Update thông tin voucher theo id
     * @description-EN:
     * @param:
     * */
    public VoucherEntity updateVoucher(VoucherDTO voucherDTO) {
        VoucherEntity voucherEntity = voucherRepository.findById(voucherDTO.getId()).get();
        voucherEntity.setId(voucherDTO.getId());
        voucherEntity.setCategoryVoucher(voucherDTO.getCategoryVoucher());
        voucherEntity.setNameProgram(voucherDTO.getNameProgram());
        voucherEntity.setCodeVoucher(voucherDTO.getCodeVoucher());
        voucherEntity.setStartDate(voucherDTO.getStartDate());
        voucherEntity.setEndDate(voucherDTO.getEndDate());
        voucherEntity.setDiscountPercent(voucherDTO.getDiscountPercent());
        voucherEntity.setMinValueOrder(voucherDTO.getMinValueOrder());
        voucherEntity.setAmountVoucher(voucherDTO.getAmountVoucher());
        voucherEntity.setMaxValueCanReduce(voucherDTO.getMaxValueCanReduce());
        voucherEntity.setAmountUsed(voucherEntity.getAmountUsed());
        voucherRepository.save(voucherEntity);
        return voucherEntity;
    }

    /*
     * @author: HieuMM
     * @since: 05-Jul-22 9:47 AM
     * @description-VN:  Delete voucher theo id
     * @description-EN:
     * @param:
     * */
    public boolean deleteVoucherById(Long id) {

        List<VoucherUserEntity> voucherUserEntityList = voucherUserRepository.findAllByVoucher(id);
        for (VoucherUserEntity voucherUserEntity : voucherUserEntityList) {
            voucherUserRepository.deleteById(voucherUserEntity.getId());
        }
        voucherRepository.deleteById(id);
        return true;
    }

    /*
     * @author: HieuMM
     * @since: 05-Jul-22 11:22 AM
     * @description-VN:  check code voucher đã tồn tại chưa
     * @description-EN:
     * @param:
     * */
    public boolean checkCodeVoucher(String codeVoucher) {
        List<String> codeVoucherList = voucherRepository.findAllCode();
        if (codeVoucherList.contains(codeVoucher) == true) {
            return false;
        }
        return true;
    }

    /*
     * @author: HieuMM
     * @since: 05-Jul-22 1:39 PM
     * @description-VN:  Lấy tất cả voucher
     * @description-EN:
     * @param:
     * */
    public Page<VoucherModel> findAllVoucher(Pageable pageable) {
        return  voucherRepository.findAll(pageable).map(VoucherModel::entityToDto);
    }

}
