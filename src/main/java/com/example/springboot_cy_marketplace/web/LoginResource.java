package com.example.springboot_cy_marketplace.web;


import com.example.springboot_cy_marketplace.dto.ResponseDTO;
import com.example.springboot_cy_marketplace.jwt.JwtTokenProvider;
import com.example.springboot_cy_marketplace.jwt.payload.request.LoginRequest;
import com.example.springboot_cy_marketplace.jwt.payload.response.LoginResponse;
import com.example.springboot_cy_marketplace.repository.IRoleRepository;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import com.example.springboot_cy_marketplace.security.impl.UserDetailsImpl;
import com.example.springboot_cy_marketplace.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class LoginResource {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    IUserService iUserService;

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 2:58 CH
     * @description-VN:  Đăng nhập bằng email và password
     * @description-EN:  Login with email and password
     * @param: loginRequest
     * @return:
     *
     * */
    @PostMapping("/login")
    public Object getResponseAfterLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseDTO.of(iUserService.loginSuccess(loginRequest), "Login");
    }
    /*
    * @author: HaiPhong
    * @since: 20/06/2022 2:47 CH
    * @description-VN:  Kiểm tra mật khẩu cũ có đúng hay không
    * @description-EN:  Check the old password is correct or not
    * @param: id, oldPass
    * @return: 
    *
    * */
    @GetMapping("/check-pass")
    public Object checkOldPassword(@RequestParam(value = "id") int id,
                                   @RequestParam(value = "oldpass") String oldPass){
        return ResponseDTO.of(iUserService.checkOldPassWord((long)id, oldPass),"Check old password");
    }
}
