package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.VoucherEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherModel {
    private Long id;
    private String categoryVoucher;
    private String nameProgram;
    private String codeVoucher;
    private java.sql.Date startDate;
    private java.sql.Date endDate;
    private int discountPercent;
    private int minValueOrder;
    private int amountVoucher;
    private int amountUsed;
    private int maxValueCanReduce;
    /*
     * @author: HieuMM
     * @since: 05-Jul-22 9:15 AM
     * @description-VN:  VoucherEntity to VoucherModel
     * @description-EN:
     * @param:
     * */
    public static VoucherModel entityToDto(VoucherEntity object){
        return VoucherModel.builder()
                .id(object.getId())
                .categoryVoucher(object.getCategoryVoucher())
                .nameProgram(object.getNameProgram())
                .codeVoucher(object.getCodeVoucher())
                .startDate(object.getStartDate())
                .endDate(object.getEndDate())
                .discountPercent(object.getDiscountPercent())
                .minValueOrder(object.getMinValueOrder())
                .amountVoucher(object.getAmountVoucher())
                .amountUsed(object.getAmountUsed())
                .maxValueCanReduce(object.getMaxValueCanReduce())
                .build();
    }
    public static VoucherEntity modelToEntity(VoucherModel object){
        return VoucherEntity.builder()
                .id(object.getId())
                .categoryVoucher(object.getCategoryVoucher())
                .nameProgram(object.getNameProgram())
                .codeVoucher(object.getCodeVoucher())
                .startDate(object.getStartDate())
                .endDate(object.getEndDate())
                .discountPercent(object.getDiscountPercent())
                .minValueOrder(object.getMinValueOrder())
                .amountVoucher(object.getAmountVoucher())
                .amountUsed(object.getAmountUsed())
                .maxValueCanReduce(object.getMaxValueCanReduce())
                .build();
    }
}
