package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.VoucherUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherUserDTO {
    private Long id;
    private Long userId;
    private Long voucherId;
    private Boolean status;
    /*
    * @author: HieuMM
    * @since: 05-Jul-22 9:17 AM
    * @description-VN:  convert VoucherUserEntity to VoucherUserDTO
    * @description-EN:
    * @param:
    * */
    public static VoucherUserDTO entityToDto(VoucherUserEntity object){
        return VoucherUserDTO.builder()
                .id(object.getId())
                .userId(object.getUserEntity().getId())
                .voucherId(object.getVoucherEntity().getId())
                .status(object.getStatus())
                .build();
    }
}
