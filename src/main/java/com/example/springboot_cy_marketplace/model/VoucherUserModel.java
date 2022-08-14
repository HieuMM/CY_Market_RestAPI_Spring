package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.entity.UserEntity;
import com.example.springboot_cy_marketplace.entity.VoucherUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoucherUserModel {
    private Long id;
    private Long userId;
    private Long voucherId;
    private Boolean status;
    private VoucherModel voucherModel;
    /*
     * @author: HieuMM
     * @since: 05-Jul-22 9:17 AM
     * @description-VN:  convert VoucherUserEntity to VoucherUserDTO
     * @description-EN:
     * @param:
     * */
    public static VoucherUserModel entityToDto(VoucherUserEntity object){
        return VoucherUserModel.builder()
                .id(object.getId())
                .userId(object.getUserEntity().getId())
                .voucherId(object.getVoucherEntity().getId())
                .status(object.getStatus())
                .voucherModel(object.getUserEntity() != null ? VoucherModel.entityToDto(object.getVoucherEntity()) : null )
                .build();
    }


}
