package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.entity.RoleEntity;
import com.example.springboot_cy_marketplace.model.RoleModel;
import com.example.springboot_cy_marketplace.services.IRoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {
    @Override
    public List<RoleEntity> findAll() {
        return null;
    }

    @Override
    public Page<RoleEntity> findAll(Pageable page) {
        return null;
    }

    @Override
    public RoleEntity findById(Long id) {
        return null;
    }

    @Override
    public RoleEntity add(RoleModel model) {
        return null;
    }

    @Override
    public List<RoleEntity> add(List<RoleModel> model) {
        return null;
    }

    @Override
    public RoleEntity update(RoleModel model) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public boolean deleteByIds(List<Long> id) {
        return false;
    }
}
