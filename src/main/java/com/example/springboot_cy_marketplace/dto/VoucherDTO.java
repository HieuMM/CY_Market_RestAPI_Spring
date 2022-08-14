package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.VoucherEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTO {
    private Long id;
    private String categoryVoucher;
    private String nameProgram;
    private String codeVoucher;
    private Date startDate;
    private Date endDate;
    private int discountPercent;
    private int minValueOrder;
    private int amountVoucher;
    private int maxValueCanReduce;
    /*
    * @author: HieuMM
    * @since: 05-Jul-22 9:15 AM
    * @description-VN:  VoucherEntity to VoucherDTO
    * @description-EN:
    * @param:
    * */
    public static VoucherDTO entityToDto(VoucherEntity object){
        return VoucherDTO.builder()
                .id(object.getId())
                .categoryVoucher(object.getCategoryVoucher())
                .nameProgram(object.getNameProgram())
                .codeVoucher(object.getCodeVoucher())
                .startDate(object.getStartDate())
                .endDate(object.getEndDate())
                .discountPercent(object.getDiscountPercent())
                .minValueOrder(object.getMinValueOrder())
                .amountVoucher(object.getAmountVoucher())
                .maxValueCanReduce(object.getMaxValueCanReduce())
                .build();
    }
}

