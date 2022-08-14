package com.example.springboot_cy_marketplace.security.impl;

import com.example.springboot_cy_marketplace.entity.RoleEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserDetailsImpl implements UserDetails {
     UserEntity userEntity;
    // Khởi tạo một đối tượng UserDetails từ UserEntity
    public UserDetailsImpl(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
    //  Lấy ra một danh sách các quyền của người dùng / Get a list of user role
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<RoleEntity> roles = userEntity.getRoleEntityList();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (RoleEntity role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
    //  Lấy thông tin mật khẩu của người dùng / Get user's password information
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }
    //  Lấy thông tin email của người dùng  /   Get user's email information
    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }
    //  Kiểm tra tài khoản đã hết hạn hay chưa  /   Check if the account has expired or not
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //  Kiểm tra tài khoản có bị khóa hay không   /   Check if the account is locked or not
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //  Kiểm trả thông tin xác thực đã hết hạn hay chưa /   Check if the credential has expired or not
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //  Kiểm tra tài khoản đã được kích hoạt hay chưa /   Check if the credential has expired or not
    @Override
    public boolean isEnabled() {
        return true;
    }

}
