package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.entity.WardEntity;
import com.example.springboot_cy_marketplace.model.WardModel;
import com.example.springboot_cy_marketplace.repository.IWardRepository;
import com.example.springboot_cy_marketplace.services.IWardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WardServiceImpl implements IWardService {
    @Autowired
    IWardRepository wardRepository;

    public WardEntity toEntity(WardModel wardModel) {
        WardEntity wardEntity = new WardEntity();
        wardEntity.setId(wardModel.getId());
        wardEntity.setName(wardModel.getName());
        wardEntity.setType(wardModel.getType());
        // wardEntity.setDistrict(wardModel.getDistrict());

        return wardEntity;
    }

    @Override
    public List<WardEntity> findAll() {
        return null;
    }

    @Override
    public Page<WardEntity> findAll(Pageable page) {
        return null;
    }


    /*
     * @author: HaiPhong
     * @since: 10/06/2022 5:00 CH
     * @description-VN:  Tìm kiếm phường xã
     * @description-EN:  Find ward
     * @param: id
     * @return: WardEntity
     *
     * */
    @Override
    public WardEntity findById(Long id) {
        Optional<WardEntity> optionalWardEntity = wardRepository.findById(id);
        if (optionalWardEntity.isPresent()) {
            return optionalWardEntity.get();
        } else {
            return null;
        }
    }

    @Override
    public WardEntity add(WardModel model) {
        return null;
    }

    @Override
    public List<WardEntity> add(List<WardModel> model) {
        return null;
    }

    @Override
    public WardEntity update(WardModel model) {
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
