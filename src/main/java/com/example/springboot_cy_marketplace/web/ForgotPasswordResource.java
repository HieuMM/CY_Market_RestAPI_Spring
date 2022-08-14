package com.example.springboot_cy_marketplace.web;

import com.example.springboot_cy_marketplace.dto.ResetPasswordDTO;
import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/forgotpassword")
public class ForgotPasswordResource {
    @Autowired
    UserServiceImpl userService;

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 2:44 CH
     * @description-VN:  Gửi mail có kèm link để người dùng thay đổi mật khẩu về mail người dùng nhập vào
     * @description-EN:  Send an email with a link for the user to change the password to the email user entered
     * @param: resetPasswordDTO
     * @return:
     *
     * */
    @PostMapping("/send-mail")
    public Object forgotPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws UnsupportedEncodingException {
        userService.sendMailResetPassword(resetPasswordDTO.getEmail());
        return ResponseDTO.of(resetPasswordDTO, "Send mail reset password");
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 2:57 CH
     * @description-VN:  Thay đổi mật khẩu
     * @description-EN:  Reset password
     * @param: resetPasswordDTO
     * @return:
     *
     * */
    @PostMapping("/reset-password")
    public Object resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return ResponseDTO.of(userService.resetPassword(resetPasswordDTO), "Reset password");
    }

}
