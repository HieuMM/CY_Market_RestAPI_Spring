package com.example.springboot_cy_marketplace.security.impl;

import com.example.springboot_cy_marketplace.entity.UserEntity;
import com.example.springboot_cy_marketplace.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserRepository iUserRepository;
    //  Tìm kiếm user có tồn tại trong hệ thống thông qua thông tin email đăng nhập / Search for a user that exists in the system through login email information
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = iUserRepository.findByEmail(username);
        if (username == null){
            throw new UsernameNotFoundException("User not found !");
        }
        return new UserDetailsImpl(userEntity);     // Từ UserEntity tạo mới một đối tượng UserDetails / From UserEntity create a new UserDetails object
    }
}
