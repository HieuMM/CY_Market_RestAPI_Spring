package com.example.springboot_cy_marketplace.model;

import com.example.springboot_cy_marketplace.dto.UserDTO;
import com.example.springboot_cy_marketplace.entity.AddressEntity;
import com.example.springboot_cy_marketplace.entity.RoleEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class này đại diện cho lớp thứ 2 trong mô hình DTO - Model - Entity
 * This class stands for 2nd class in diagram DTO - Model - Entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private Long id;
    private String fullName;
    private String email;
    private boolean enabled;
    private String resetPassWordToken;
    private boolean accountNonLocked;
    private int failedAttempt;
    private Date lockTime;
    private boolean rememberme;
    private String avatarUrl;
    private String role;
    private String createdDate;
    private List<AddressModel> addressList;
    private String phoneNumber;

    public static UserModel entityToModel(UserEntity object){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<String> roleNames = new ArrayList<>();
        String biggestRole = "";
        for(RoleEntity r : object.getRoleEntityList()){
            roleNames.add(r.getName());
        }
        if(roleNames.contains("ROLE_ADMIN")) {
            biggestRole = "ROLE_ADMIN";
        }else if(roleNames.contains("ROLE_SELLER")){
            biggestRole = "ROLE_SELLER";
        }else {
            biggestRole = "ROLE_BUYER";
        }

        List<AddressModel> addressList = new ArrayList<>();
        for(AddressEntity a : object.getAddressEntityList()){
            addressList.add(AddressModel.entityToModel(a));
        }
        return UserModel.builder()
                .id(object.getId())
                .fullName(object.getFullName())
                .email(object.getEmail())
                .enabled(object.isEnabled())
                .resetPassWordToken(object.getResetPassWordToken())
                .accountNonLocked(object.isAccountNonLocked())
                .failedAttempt(object.getFailedAttempt())
                .lockTime(object.getLockTime())
                .rememberme(object.isRememberme())
                .avatarUrl(object.getAvatar())
                .createdDate(sdf.format(object.getCreateDate()))
                .role(biggestRole)
                .addressList(addressList)
                .phoneNumber(object.getPhoneNumber())
                .build();

    }

    public static UserDTO toDTO(UserModel object){
        return UserDTO.builder()
                .id(object.getId())
                .fullName(object.getFullName())
                .email(object.getEmail())
                .enabled(object.isEnabled())
                .resetPassWordToken(object.getResetPassWordToken())
                .accountNonLocked(object.isAccountNonLocked())
                .failedAttempt(object.getFailedAttempt())
                .lockTime(object.getLockTime())
                .rememberme(object.isRememberme())
                .avatarUrl(object.getAvatarUrl())
                .build();

    }
}
