package com.example.springboot_cy_marketplace.dto;

import com.example.springboot_cy_marketplace.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
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

    public static UserEntity dtoToEntity(UserDTO object){
        return UserEntity.builder()
                .id(object.getId())
                .fullName(object.getFullName())
                .email(object.getEmail())
                .enabled(object.isEnabled())
                .resetPassWordToken(object.getResetPassWordToken())
                .accountNonLocked(object.isAccountNonLocked())
                .failedAttempt(object.getFailedAttempt())
                .lockTime(object.getLockTime())
                .rememberme(object.isRememberme())
                .avatar(object.getAvatarUrl())
                .build();
    }
}
