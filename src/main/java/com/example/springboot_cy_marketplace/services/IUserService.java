package com.example.springboot_cy_marketplace.services;

import com.example.springboot_cy_marketplace.dto.UserDTO;
import com.example.springboot_cy_marketplace.dto.UserInfoDTO;
import com.example.springboot_cy_marketplace.jwt.payload.request.LoginRequest;
import com.example.springboot_cy_marketplace.jwt.payload.request.RegisterRequest;
import com.example.springboot_cy_marketplace.model.UserModel;

public interface IUserService extends IBaseService<UserModel, UserDTO, Long> {
    boolean add(RegisterRequest userSignup);

    Object loginSuccess(LoginRequest loginRequest);

    boolean checkOldPassWord(Long id, String oldPass);

    boolean changePassword(UserInfoDTO userInfoDTO);

    boolean resetUserPassword(UserInfoDTO userInfoDTO);

}
