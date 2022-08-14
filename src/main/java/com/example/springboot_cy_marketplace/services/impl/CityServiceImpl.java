package com.example.springboot_cy_marketplace.services.impl;

import com.example.springboot_cy_marketplace.entity.CityEntity;
import com.example.springboot_cy_marketplace.model.CityModel;
import com.example.springboot_cy_marketplace.repository.ICityRepository;
import com.example.springboot_cy_marketplace.services.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements ICityService {
    @Autowired
    ICityRepository iCityRepository;

    @Override
    public List<CityEntity> findAll() {
        return null;
    }

    @Override
    public Page<CityEntity> findAll(Pageable page) {
        return null;
    }

    @Override
    public CityEntity findById(Long id) {
        return null;
    }

    /*
     * @author: HaiPhong
     * @since: 10/06/2022 4:17 CH
     * @description-VN:  Tìm kiếm thành phố
     * @description-EN:  City search
     * @param: id
     * @return: CityModel
     *
     * */
    @Override
    public CityModel findByID(Long id) {
        Optional<CityEntity> optionalCityEntity = iCityRepository.findById(id);
        if (optionalCityEntity.isPresent()) {
            return CityModel.entityToModel(optionalCityEntity.get());
        } else {
            return null;
        }
    }

    public CityEntity toEntity(CityModel cityModel) {
        CityEntity cityEntity = new CityEntity();
        cityEntity.setId(cityModel.getId());
        cityEntity.setName(cityModel.getName());
        cityEntity.setType(cityModel.getType());
        cityEntity.setSlug(cityModel.getSlug());
        return cityEntity;
    }

    @Override
    public CityEntity add(CityModel model) {
        return null;
    }

    @Override
    public List<CityEntity> add(List<CityModel> model) {
        return null;
    }

    @Override
    public CityEntity update(CityModel model) {
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
